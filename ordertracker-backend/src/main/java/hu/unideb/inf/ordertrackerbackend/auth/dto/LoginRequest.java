package hu.unideb.inf.ordertrackerbackend.auth.dto;

import lombok.Data;

@Data
public class LoginRequest {
    String username;
    String password;
}
