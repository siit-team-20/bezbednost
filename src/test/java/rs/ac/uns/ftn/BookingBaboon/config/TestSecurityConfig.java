package rs.ac.uns.ftn.BookingBaboon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class TestSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // Create a test user or return a mock UserDetails
            return User.builder()
                    .username(username)
                    .password("password")
                    .roles("USER")
                    .build();
        };
    }
}