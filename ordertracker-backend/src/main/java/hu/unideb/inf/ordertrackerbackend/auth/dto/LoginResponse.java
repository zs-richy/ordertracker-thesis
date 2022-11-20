package hu.unideb.inf.ordertrackerbackend.auth.dto;

import hu.unideb.inf.ordertrackerbackend.api.dto.common.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class LoginResponse extends BaseResponse {
    String token;
    String username;
    List<String> roles;
    long expires;
}
