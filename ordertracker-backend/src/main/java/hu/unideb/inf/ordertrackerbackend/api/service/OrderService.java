package hu.unideb.inf.ordertrackerbackend.api.service;

import hu.unideb.inf.ordertrackerbackend.api.dto.order.*;
import hu.unideb.inf.ordertrackerbackend.api.enums.OrderStatus;
import hu.unideb.inf.ordertrackerbackend.api.model.Order;
import hu.unideb.inf.ordertrackerbackend.api.model.OrderItem;
import hu.unideb.inf.ordertrackerbackend.api.model.Product;
import hu.unideb.inf.ordertrackerbackend.api.repository.OrderItemRepository;
import hu.unideb.inf.ordertrackerbackend.api.repository.OrderRepository;
import hu.unideb.inf.ordertrackerbackend.api.repository.ProductRepository;
import hu.unideb.inf.ordertrackerbackend.auth.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.NoPermissionException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.*;

@Component
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    JwtTokenUtil tokenUtil;

    @Transactional
    public ResponseEntity<NewOrderResponse> addNewOrder(NewOrderDto orderDto, String token) {
        NewOrderResponse newOrderResponse = new NewOrderResponse();

        try {
            Order order = orderRepository.save(new Order(OrderStatus.CREATED));
            Set<OrderItem> orderItems = new HashSet<>();

            Order orderForLambda = order;
            orderDto.getOrderItems().forEach((key, val) -> {
                Product product = productRepository.findById(key).get();
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(orderForLambda);
                orderItem.setProduct(product);
                orderItem.setPrice(product.getPrice());
                orderItem.setCount(val);

                orderItemRepository.save(orderItem);

                orderItems.add(orderItem);
            });

            order.setUserId(tokenUtil.getUserIdFromToken(tokenUtil.getAllClaimsFromToken(token)));
            order.setOrderItems(orderItems);

            order = orderRepository.save(order);

            newOrderResponse.setOrderId(order.getId());
            newOrderResponse.setOrderStatus(order.getStatus());

            return ResponseEntity.ok(newOrderResponse);
        } catch (Exception e) {
            e.printStackTrace();
            newOrderResponse.getErrorMessages().add("Error creating new order.");

            return ResponseEntity.badRequest().body(newOrderResponse);
        }
    }

    public ResponseEntity<GetOrdersResponse> listOrders() {
        GetOrdersResponse getOrdersResponse = new GetOrdersResponse();

        try {
            List<Order> orders = orderRepository.findAll();
            orders.forEach(Order::calculateSumOfItems);

            getOrdersResponse.setOrders(orders);
        } catch (Exception e) {
            e.printStackTrace();
            getOrdersResponse.getErrorMessages().add("Error while fetching Orders.");
            return ResponseEntity.badRequest().body(getOrdersResponse);
        }

        return ResponseEntity.ok(getOrdersResponse);
    }

    /**
     * Lists orders of the given user, the user is calculated by the auth token
     */
    public ResponseEntity<GetOrdersResponse> listOrdersByUser(String token) {
        GetOrdersResponse getOrdersResponse = new GetOrdersResponse();

        try {
            Long userId = tokenUtil.getUserIdFromToken(tokenUtil.getAllClaimsFromToken(token));
            List<Order> orders = orderRepository.findAllByUserId(userId);
            orders.forEach(Order::calculateSumOfItems);
            orders.forEach(Order::removeOrderItems);

            getOrdersResponse.setOrders(orders);
        } catch (Exception e) {
            e.printStackTrace();
            getOrdersResponse.getErrorMessages().add("Error while fetching Orders.");
            return ResponseEntity.badRequest().body(getOrdersResponse);
        }

        return ResponseEntity.ok(getOrdersResponse);
    }

    @Transactional
    public ResponseEntity<UpdateOrderStatusResponse> updateOrderStatus(UpdateOrderStatusDto orderStatusDto) {
        UpdateOrderStatusResponse updateOrderStatusResponse = new UpdateOrderStatusResponse();

        try {
            orderRepository.updateStatusById(orderStatusDto.getOrderId(), orderStatusDto.getNewStatus());
            orderRepository.updateStatusModifyById(orderStatusDto.getOrderId(), LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
            updateOrderStatusResponse.getErrorMessages().add("Error updating order status.");
            return ResponseEntity.badRequest().body(updateOrderStatusResponse);
        }

        return ResponseEntity.ok(updateOrderStatusResponse);
    }

    /**
     * Attempts to fetch the user's order with the given ID
     *
     * NoPermissionException is thrown, if the user is not the 'owner' of the order.
     *
     * @param token auth token to calculate the user information
     * @param orderId ID of the order to fetch*
     */
    public ResponseEntity<GetOrderByIdResponse> findUserOrderById(String token, String orderId) {
        GetOrderByIdResponse orderResponse = new GetOrderByIdResponse();

        try {
            Long userId = tokenUtil.getUserIdFromToken(tokenUtil.getAllClaimsFromToken(token));
            Optional<Order> order = orderRepository.findById(Long.valueOf(orderId));

            if (order.isPresent()) {
                if (!order.get().getUserId().equals(userId)) {
                    throw new NoPermissionException();
                }
                order.get().calculateSumOfItems();
                orderResponse.setOrder(order.get());
                return ResponseEntity.ok(orderResponse);
            } else {
                orderResponse.getErrorMessages().add("Order not found.");
                return ResponseEntity.badRequest().body(orderResponse);
            }

        } catch (NoPermissionException e) {
            e.printStackTrace();
            orderResponse.getErrorMessages().add("No permission to access the order with the given ID.");
            return ResponseEntity.badRequest().body(orderResponse);
        } catch (Exception e) {
            e.printStackTrace();
            orderResponse.getErrorMessages().add("Error while fetching order by ID.");
            return ResponseEntity.badRequest().body(orderResponse);
        }
    }

    public ResponseEntity<GetOrderByIdResponse> findOrderById(String orderId) {
        GetOrderByIdResponse orderResponse = new GetOrderByIdResponse();

        try {
            Optional<Order> order = orderRepository.findById(Long.valueOf(orderId));

            if (order.isPresent()) {
                orderResponse.setOrder(order.get());
                return ResponseEntity.ok(orderResponse);
            } else {
                orderResponse.getErrorMessages().add("Order not found.");
                return ResponseEntity.badRequest().body(orderResponse);
            }

        } catch (Exception e) {
            e.printStackTrace();
            orderResponse.getErrorMessages().add("Error while fetching order by ID.");
            return ResponseEntity.badRequest().body(orderResponse);
        }
    }

    /**
     * Fetches orders which were placed on a given date
     *
     * @param getOrdersByDateRequest DTO containing the date to query.
     */
    public ResponseEntity<GetOrdersResponse> findAllByDate(GetOrdersByDateRequest getOrdersByDateRequest) {
        GetOrdersResponse getOrdersResponse = new GetOrdersResponse();

        try {
            LocalDateTime dateFrom = getOrdersByDateRequest.getDate().with(LocalTime.of(0,0));
            LocalDateTime dateTo = getOrdersByDateRequest.getDate().with(LocalTime.of(23,59));

            List<Order> orders = orderRepository.findAllByDate(dateFrom, dateTo);
            orders.forEach(Order::calculateSumOfItems);

            getOrdersResponse.setOrders(orders);
        } catch (Exception e) {
            e.printStackTrace();
            getOrdersResponse.getErrorMessages().add("Error while fetching Orders.");
            return ResponseEntity.badRequest().body(getOrdersResponse);
        }

        return ResponseEntity.ok(getOrdersResponse);
    }

}
