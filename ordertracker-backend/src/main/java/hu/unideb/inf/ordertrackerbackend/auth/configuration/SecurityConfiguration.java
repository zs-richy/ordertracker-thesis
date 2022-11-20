package hu.unideb.inf.ordertrackerbackend.auth.configuration;

import hu.unideb.inf.ordertrackerbackend.auth.repository.UserRepository;
import hu.unideb.inf.ordertrackerbackend.auth.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private BCryptPasswordEncoder passwordEncoder;

    private UserDetailsService userDetailsService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //Permits access to the given url patterns for all visitors.
        http
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests()
                .antMatchers("/auth/**", "/v3/**",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/webjars/**").permitAll();

        //Restricts the API services with interceptor Beans.
        http
                .antMatcher("/api/**")
                .addFilterBefore(exceptionHandlerBean(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(tokenFilterBean(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated()
                .and().exceptionHandling();

        http.cors();

        return http.build();
    }
    public JwtTokenFilter tokenFilterBean() {
        return new JwtTokenFilter(jwtTokenUtil, userRepository);
    }
    public FilterExceptionHandler exceptionHandlerBean() {
        return new FilterExceptionHandler();
    }

}
