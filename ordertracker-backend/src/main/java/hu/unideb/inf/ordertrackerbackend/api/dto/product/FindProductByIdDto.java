package hu.unideb.inf.ordertrackerbackend.api.dto.product;

import lombok.Data;

@Data
public class FindProductByIdDto {
    private Long id;
    private boolean withBinary;
}
