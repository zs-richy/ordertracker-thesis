package hu.unideb.inf.ordertrackerbackend.auth.controller;

import hu.unideb.inf.ordertrackerbackend.auth.dto.LoginRequest;
import hu.unideb.inf.ordertrackerbackend.auth.dto.LoginResponse;
import hu.unideb.inf.ordertrackerbackend.auth.dto.SignupRequest;
import hu.unideb.inf.ordertrackerbackend.auth.dto.SignupResponse;
import hu.unideb.inf.ordertrackerbackend.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> registerUser(@RequestBody SignupRequest signupRequest) {
        return authService.signup(signupRequest);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return authService.authenticate(loginRequest);
    }
}
