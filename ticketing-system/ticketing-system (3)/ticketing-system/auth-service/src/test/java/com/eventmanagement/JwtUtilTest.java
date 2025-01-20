package com.eventmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {
    @MockBean
    private final JwtUtil jwtUtil = new JwtUtil();

    @Test
    public void testGenerateToken() {
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        String token = jwtUtil.generateToken(user);
        assertNotNull(token);
    }

    @Test
    public void testValidateToken() {
        UserEntity user = new UserEntity();
        user.setUsername("testuser");

        String token = jwtUtil.generateToken(user);
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    public void testValidateTokenExpired() {
        // Simuler un token expiré (en général, il faut générer un token avec une date d'expiration passée)
        String expiredToken = "expiredToken";  // Vous devez générer un token expiré pour ce test
        assertFalse(jwtUtil.validateToken(expiredToken));
    }
}
