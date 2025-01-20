package com.eventmanagement;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestSecurityConfig.class})  // Assurez-vous de charger uniquement la config de sécurité pour les tests
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void testRegisterUser() {
        // Préparer les données
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity registeredUser = userService.registerUser(user);

        // Vérifier que le mot de passe a été encodé
        verify(passwordEncoder).encode("password123");
        assertEquals("encodedPassword", registeredUser.getPassword());
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        // Simuler un utilisateur déjà existant
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // Vérifier que l'exception est lancée
        assertThrows(RuntimeException.class, () -> userService.registerUser(user));
    }
}
