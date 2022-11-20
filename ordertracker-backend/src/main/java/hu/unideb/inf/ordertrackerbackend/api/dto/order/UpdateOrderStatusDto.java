package hu.unideb.inf.ordertrackerbackend.api.dto.order;

import hu.unideb.inf.ordertrackerbackend.api.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusDto {
    private Long orderId;
    private OrderStatus newStatus;
}
