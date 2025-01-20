package com.eventmanagement;



import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    public AuthController(UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserEntity user) {
        // Log des données reçues
        logger.info("Données reçues pour l'inscription : {}", user);

        // Appeler le service pour enregistrer l'utilisateur
        userService.registerUser(user);

        // Retourner une réponse de succès
        return ResponseEntity.ok("Utilisateur enregistré avec succès !");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserEntity user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        // Récupérer l'utilisateur complet depuis votre base de données
        UserEntity fullUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        // Générer le token sans inclure le mot de passe
        String token = jwtUtil.generateToken(fullUser);

        // Retourner le token et d'autres détails si nécessaire
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("username", fullUser.getUsername());
        response.put("email", fullUser.getEmail());
        response.put("id", String.valueOf(fullUser.getId()));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @GetMapping("/user/{email}")
    public UserEntity getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }


    @GetMapping("/validate-token")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token) {
        try {
            // Supprimer "Bearer " du token pour ne conserver que le token JWT
            String jwtToken = token.substring(7);
            boolean isValid = jwtUtil.validateToken(jwtToken);

            if (isValid) {
                return ResponseEntity.ok("Token valide");
            } else {
                return ResponseEntity.status(401).body("Token invalide");
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erreur lors de la validation du token : " + e.getMessage());
        }
    }
}
