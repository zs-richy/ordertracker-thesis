package hu.unideb.inf.ordertrackerbackend.auth.dto;


import lombok.Data;

@Data
public class SignupRequest {
    String username;
    String password;
}
