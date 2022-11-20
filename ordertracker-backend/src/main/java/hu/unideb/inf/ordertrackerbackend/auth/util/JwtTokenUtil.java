package hu.unideb.inf.ordertrackerbackend.auth.util;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import hu.unideb.inf.ordertrackerbackend.auth.model.Role;
import hu.unideb.inf.ordertrackerbackend.auth.model.UserDetailsWrapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtTokenUtil implements Serializable {
    public static final long tokenExpiresIn = 60 * 60;
    private static final String roleStringDelimiter = ";";
    private static final String ROLES = "roles";
    private static final String USERID = "userid";
    private static final String UUID = "uuid";
    private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateToken(UserDetailsWrapper userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ROLES, authoritiesToString(userDetails.getUserDetails().getAuthorities()));
        claims.put(USERID, userDetails.getUserId());
        claims.put(UUID, java.util.UUID.randomUUID());
        return generateToken(claims, userDetails.getUserDetails().getUsername());
    }

    private String generateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiresIn * 1000))
                .signWith(secretKey)
                .compact();

    }

    public Jws<Claims> getAllClaimsFromToken(String token) {
        try {
            Jws<Claims> jws;
            jws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return jws;

        } catch (Exception e) {
            return null;
        }

    }

    public Boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean isTokenExpired(Jws<Claims> claims) {
        final Date expiration = getExpirationDateFromClaims(claims);

        return expiration.after(new Date());
    }

    public Date getExpirationDateFromClaims(String token) {
        return getExpirationDateFromClaims(getAllClaimsFromToken(token));
    }

    public Date getExpirationDateFromClaims(Jws<Claims> claims) {
        return claims.getBody().getExpiration();
    }

    public String getUsernameFromToken(Jws<Claims> claims) {
        return claims.getBody().getSubject();
    }

    public Long getUserIdFromToken(Jws<Claims> claims) {
        return ((Integer) claims.getBody().get(USERID)).longValue();
    }

    public List<String> getUserRolesFromToken(String token) {
        return getUserRolesFromClaims(getAllClaimsFromToken(token));
    }

    public List<String> getUserRolesFromClaims(Jws<Claims> claims) {
        return authorityStringToRoles((String) claims.getBody().get(ROLES))
                .stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public Boolean isAdmin(Jws<Claims> claims) {
        String authorities = (String) claims.getBody().get(ROLES);

        if (authorities == null || authorities.isEmpty()) {
            return false;
        }

        List<Role> roles = authorityStringToRoles(authorities);

        return roles.stream().anyMatch(role -> role == Role.ADMIN);
    }

    private String authoritiesToString(Collection<? extends GrantedAuthority> authorities) {
        StringJoiner stringJoiner = new StringJoiner(roleStringDelimiter);

        if (authorities != null && !authorities.isEmpty()) {
            authorities.forEach(grantedAuthority ->
                    stringJoiner.add(grantedAuthority.getAuthority())
            );
        }

        return stringJoiner.toString();
    }

    private List<Role> authorityStringToRoles(String authorityString) {
        List<Role> roleList = new ArrayList<>();

        String[] splitted = authorityString.split(roleStringDelimiter);

        Arrays.stream(splitted).forEach(splitString ->
                roleList.add(Role.valueOf(splitString))
        );

        return roleList;
    }

}
