package com.eventmanagement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Disable CSRF for tests
                .authorizeRequests(authz -> authz
                        .requestMatchers("/reservation/**", "/events/**").permitAll()  // Permit all requests to /reservation/** and /events/**
                        .anyRequest().permitAll())  // Allow all other requests in the test context
                .sessionManagement(session -> session.disable());  // Disable session management if needed
        return http.build();
    }
}
