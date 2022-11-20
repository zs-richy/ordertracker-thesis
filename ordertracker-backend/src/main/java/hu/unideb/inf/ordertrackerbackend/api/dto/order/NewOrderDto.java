package hu.unideb.inf.ordertrackerbackend.api.dto.order;

import hu.unideb.inf.ordertrackerbackend.api.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class NewOrderDto {
    Map<Long, Integer> orderItems;
}
