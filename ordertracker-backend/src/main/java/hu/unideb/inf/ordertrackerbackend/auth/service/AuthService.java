package hu.unideb.inf.ordertrackerbackend.auth.service;

import hu.unideb.inf.ordertrackerbackend.auth.dto.LoginRequest;
import hu.unideb.inf.ordertrackerbackend.auth.dto.LoginResponse;
import hu.unideb.inf.ordertrackerbackend.auth.dto.SignupRequest;
import hu.unideb.inf.ordertrackerbackend.auth.dto.SignupResponse;
import hu.unideb.inf.ordertrackerbackend.auth.model.Role;
import hu.unideb.inf.ordertrackerbackend.auth.model.User;
import hu.unideb.inf.ordertrackerbackend.auth.repository.UserRepository;
import hu.unideb.inf.ordertrackerbackend.auth.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuthService {
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MyUserDetailsService userDetailsService;
    @Autowired
    private CustomAuthenticationManager authenticator;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public ResponseEntity<SignupResponse> signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            SignupResponse signupResponse = new SignupResponse();
            signupResponse.getErrorMessages().add("User already exists!");

            return ResponseEntity.badRequest().body(signupResponse);
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(encoder.encode(request.getPassword()));
        newUser.setRoles(List.of(Role.USER));
        userRepository.save(newUser);

        return ResponseEntity.ok(new SignupResponse());
    }

    public ResponseEntity<LoginResponse> authenticate(LoginRequest request) {
        LoginResponse response = new LoginResponse();

        try {
            authenticator.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            response.getErrorMessages().add("Wrong username/password combination.");

            return ResponseEntity.badRequest().body(response);
        }

        String token = jwtTokenUtil.generateToken(userDetailsService.loadUserByUsernameExt(request.getUsername()));

        response.setToken(token);
        response.setUsername(request.getUsername());
        response.setExpires(jwtTokenUtil.getExpirationDateFromClaims(token).getTime());
        response.setRoles(jwtTokenUtil.getUserRolesFromToken(token));


        return ResponseEntity.ok(response);
    }

}
