package hu.unideb.inf.ordertrackerbackend.api.controller;

import hu.unideb.inf.ordertrackerbackend.api.dto.order.*;
import hu.unideb.inf.ordertrackerbackend.api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order/")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {
    @Autowired
    OrderService orderService;

    private static final String AUTHORIZATION = "Authorization";
    private static final String ID = "id";

    @PostMapping("/new-order")
    public ResponseEntity<NewOrderResponse> newOrder(@RequestBody NewOrderDto newOrderDto,
                                   @RequestHeader(AUTHORIZATION) String token) {
            return orderService.addNewOrder(newOrderDto, token);
    }

    @GetMapping("/admin/orders")
    public ResponseEntity<GetOrdersResponse> getOrders() {
        return orderService.listOrders();
    }

    @GetMapping("/orders-no-join")
    public ResponseEntity<GetOrdersResponse> getOrdersNoJoin(@RequestHeader(AUTHORIZATION) String token) {
        return orderService.listOrdersByUser(token);
    }

    @PostMapping("/admin/update-status")
    public ResponseEntity<UpdateOrderStatusResponse> updateOrderStatus(@RequestBody UpdateOrderStatusDto orderStatusDto) {
        return orderService.updateOrderStatus(orderStatusDto);
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<GetOrderByIdResponse> getOrderById(@RequestHeader(AUTHORIZATION) String token, @PathVariable(ID) String orderId) {
        return orderService.findUserOrderById(token, orderId);
    }

    @GetMapping("/admin/order/{id}")
    public ResponseEntity<GetOrderByIdResponse> getOrderById(@PathVariable(ID) String orderId) {
        return orderService.findOrderById(orderId);
    }

    @PostMapping("/admin/orders-by-date")
    public ResponseEntity<GetOrdersResponse> getOrdersByDate(@RequestBody GetOrdersByDateRequest getOrdersByDateRequest) {
        return orderService.findAllByDate(getOrdersByDateRequest);
    }

}
