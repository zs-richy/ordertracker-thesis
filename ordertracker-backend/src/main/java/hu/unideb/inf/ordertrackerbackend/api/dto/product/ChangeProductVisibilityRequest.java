package hu.unideb.inf.ordertrackerbackend.api.dto.product;

import lombok.Data;
import lombok.Getter;

@Data
public class ChangeProductVisibilityRequest {
    private boolean isVisible = true;
}
