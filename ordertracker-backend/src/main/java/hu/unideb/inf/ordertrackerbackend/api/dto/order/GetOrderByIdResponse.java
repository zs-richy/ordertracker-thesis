package hu.unideb.inf.ordertrackerbackend.api.dto.order;

import hu.unideb.inf.ordertrackerbackend.api.dto.common.BaseResponse;
import hu.unideb.inf.ordertrackerbackend.api.model.Order;
import lombok.Data;

@Data
public class GetOrderByIdResponse extends BaseResponse {
    private Order order;
}
