package hu.unideb.inf.ordertrackerbackend.api.dto.order;

import hu.unideb.inf.ordertrackerbackend.api.dto.common.BaseResponse;
import hu.unideb.inf.ordertrackerbackend.api.model.Order;
import lombok.Data;

import java.util.List;

@Data
public class GetOrdersResponse extends BaseResponse {
    private List<Order> orders;
}
