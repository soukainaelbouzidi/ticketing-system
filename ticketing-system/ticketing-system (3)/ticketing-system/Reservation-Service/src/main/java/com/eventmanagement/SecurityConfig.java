package com.eventmanagement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Désactiver CSRF si nécessaire (API REST, tests)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/reservation/**", "/events/**").permitAll() // Autoriser les requêtes liées aux réservations
                        .anyRequest().authenticated() // Authentification requise pour toutes les autres requêtes
                );
        return http.build();
    }
}
