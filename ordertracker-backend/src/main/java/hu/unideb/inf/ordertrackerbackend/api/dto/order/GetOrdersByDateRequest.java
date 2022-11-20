package hu.unideb.inf.ordertrackerbackend.api.dto.order;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetOrdersByDateRequest {
    private LocalDateTime date;
}
