package hu.unideb.inf.ordertrackerbackend.auth.configuration;

import hu.unideb.inf.ordertrackerbackend.auth.model.Role;
import hu.unideb.inf.ordertrackerbackend.auth.model.User;
import hu.unideb.inf.ordertrackerbackend.auth.repository.UserRepository;
import hu.unideb.inf.ordertrackerbackend.auth.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


public class JwtTokenFilter implements Filter {

    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;

    private static final RequestMatcher adminUrlFitler = new AntPathRequestMatcher("/**/admin/**");


    public JwtTokenFilter(JwtTokenUtil tokenUtil, UserRepository userRepository) {
        this.jwtTokenUtil = tokenUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        final String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (token == null || token.isEmpty()) {
            throw new AuthenticationServiceException("Authorization header missing.");
        }

        if (!jwtTokenUtil.isTokenValid(token)) {
            throw new AuthenticationServiceException("Not a valid authorization token.");
        }

        Jws<Claims> claims = jwtTokenUtil.getAllClaimsFromToken(token);

        if (!jwtTokenUtil.isTokenExpired(claims)) {
            throw new AuthenticationServiceException("Token expired.");
        }

        if (adminUrlFitler.matches(request) && !jwtTokenUtil.isAdmin(claims)) {
            throw new AuthenticationServiceException("Forbidden - not " + Role.ADMIN);
        }

        User userDetails = userRepository
                .findByUsername(jwtTokenUtil.getUsernameFromToken(claims)).orElse(null);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails == null ? List.of() : userDetails.rolesToAuthorities());


        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}