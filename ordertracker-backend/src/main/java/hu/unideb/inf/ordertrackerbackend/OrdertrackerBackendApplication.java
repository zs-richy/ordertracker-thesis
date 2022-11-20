package hu.unideb.inf.ordertrackerbackend;

import hu.unideb.inf.ordertrackerbackend.auth.model.Role;
import hu.unideb.inf.ordertrackerbackend.auth.model.User;
import hu.unideb.inf.ordertrackerbackend.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class OrdertrackerBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdertrackerBackendApplication.class, args);
    }

    /**
     * Uncomment to create 3 default users with all Roles.
     */
//    @Bean
//    CommandLineRunner init (UserRepository userRepository){
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        return args -> {
//            List<String> names = Arrays.asList("user1", "user2", "user3");
//            ArrayList<Role> roleList = new ArrayList<>();
//            Role role1 = Role.ADMIN;
//            Role role2 = Role.USER;
//            roleList.add(role1);
//            roleList.add(role2);
//            names.forEach(name -> userRepository.save(new User(name, encoder.encode(name), roleList)));
//        };
//    }

}
