package hu.unideb.inf.ordertrackerbackend.api.dto.product;

import hu.unideb.inf.ordertrackerbackend.api.dto.common.BaseResponse;
import hu.unideb.inf.ordertrackerbackend.api.model.Product;
import lombok.Data;

import java.util.List;
@Data
public class ProductsDto extends BaseResponse {
    private List<Product> productList;
}
