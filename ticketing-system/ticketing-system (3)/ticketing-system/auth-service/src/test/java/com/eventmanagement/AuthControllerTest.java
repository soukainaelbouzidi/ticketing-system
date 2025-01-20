package com.eventmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@Import(TestSecurityConfig.class)  // Importer la configuration de sécurité pour les tests
@WebMvcTest(AuthController.class)  // Charger uniquement le contrôleur AuthController
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;  // Simulation de UserService

    @MockBean
    private JwtUtil jwtUtil;  // Simulation de JwtUtil

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    @WithMockUser(roles = "USER")  // Simuler un utilisateur authentifié avec un rôle USER
    public void testValidateToken() throws Exception {
        String token = "validToken";  // Simulez ou générez un token valide

        when(jwtUtil.validateToken(token)).thenReturn(true);  // Simuler la validation du token

        mockMvc.perform(MockMvcRequestBuilders.get("/auth/validate-token")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Token valide"));
    }

    @Test
    @WithMockUser(roles = "USER")  // Simuler un utilisateur authentifié avec un rôle USER
    public void testGetAllUsers() throws Exception {
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        List<UserEntity> users = List.of(user);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/auth/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username", is("testuser")));
    }
}
