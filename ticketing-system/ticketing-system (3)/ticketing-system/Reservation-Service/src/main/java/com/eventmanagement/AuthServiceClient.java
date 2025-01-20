package com.eventmanagement;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "AUTH-SERVICE")
public interface AuthServiceClient {

    @PostMapping("/auth/login")
    ResponseEntity<String> login(@RequestBody LoginRequest loginRequest);

    @GetMapping("/auth/validate-token")
    ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token);

    @GetMapping("/auth/user-details/{username}")
    UserResponse getUserDetails(@PathVariable("username") String username);

    // Nouvelle méthode pour extraire l'ID de l'utilisateur à partir du token
    @GetMapping("/auth/validate-token-details")
    String getUserDetailsFromToken(@RequestHeader("Authorization") String token); // Changement du chemin
}
