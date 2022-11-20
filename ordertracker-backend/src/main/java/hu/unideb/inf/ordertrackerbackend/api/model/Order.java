package hu.unideb.inf.ordertrackerbackend.api.model;

import hu.unideb.inf.ordertrackerbackend.api.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private OrderStatus status;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime statusLastModify = LocalDateTime.now();

    @Transient
    private float sumOfItems;

    @OneToMany(mappedBy = "order")
    private Set<OrderItem> orderItems;

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
    }

    public Order(OrderStatus status) {
        this.status = status;
    }

    public void calculateSumOfItems() {
        float sum = 0f;

        for (OrderItem orderItem : orderItems) {
            sum += orderItem.getPrice() * orderItem.getCount();
        }

        sumOfItems = sum;
    }

    public void removeOrderItems() {
        orderItems = null;
    }

}
