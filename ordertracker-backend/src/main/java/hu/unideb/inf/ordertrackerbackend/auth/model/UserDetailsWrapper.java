package hu.unideb.inf.ordertrackerbackend.auth.model;

import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class UserDetailsWrapper {
    private UserDetails userDetails;
    private Long userId;
}
