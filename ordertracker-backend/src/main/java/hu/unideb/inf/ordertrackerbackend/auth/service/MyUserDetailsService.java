package hu.unideb.inf.ordertrackerbackend.auth.service;

import hu.unideb.inf.ordertrackerbackend.auth.model.Role;
import hu.unideb.inf.ordertrackerbackend.auth.model.User;
import hu.unideb.inf.ordertrackerbackend.auth.model.UserDetailsWrapper;
import hu.unideb.inf.ordertrackerbackend.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return loadUserByUsernameExt(username).getUserDetails();
    }

    public UserDetailsWrapper loadUserByUsernameExt(String username) {
        UserDetailsWrapper userDetailsWrapped = new UserDetailsWrapper();

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return null;
        } else {
            User userEntiy = user.get();
            userDetailsWrapped.setUserDetails(new org.springframework.security.core.userdetails.User(
                    userEntiy.getUsername(), userEntiy.getPassword(), getGrantedAuthorities(userEntiy.getRoles())));
            userDetailsWrapped.setUserId(userEntiy.getId());
        }

        return userDetailsWrapped;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.name()));
        }

        return authorities;
    }

}
