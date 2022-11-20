package hu.unideb.inf.ordertrackerbackend.api.dto.order;

import hu.unideb.inf.ordertrackerbackend.api.dto.common.BaseResponse;
import hu.unideb.inf.ordertrackerbackend.api.enums.OrderStatus;
import lombok.Data;


@Data
public class NewOrderResponse extends BaseResponse {
    private long orderId;
    private OrderStatus orderStatus;
}
