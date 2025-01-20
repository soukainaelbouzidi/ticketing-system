package com.eventmanagement;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    // Chargez une clé secrète plus sécurisée depuis une source externe pour la production.
    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Clé générée pour l'algorithme HS256

    // Génération du token JWT
    public String generateToken(UserEntity user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("id", user.getId())
                .claim("email", user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expiration après 10 heures
                .signWith(SECRET_KEY)
                .compact();
    }

    // Extraction des informations du token (claims)
    public Map<String, Object> extractAllClaims(String token) {
        Claims claims = getClaims(token);
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("username", claims.getSubject());
        userDetails.put("id", claims.get("id", Integer.class));
        userDetails.put("email", claims.get("email", String.class));
        return userDetails;
    }

    // Récupération des claims à partir du token
    private Claims getClaims(String token) {
        return Jwts.parserBuilder() // Utilisation de la méthode moderne
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Vérification de l'expiration du token
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extraction de la date d'expiration à partir du token
    private Date extractExpiration(String token) {
        return getClaims(token).getExpiration();
    }

    // Validation du token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()  // Utilisation de la méthode moderne pour le parsing
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token); // Si le token est valide, cette ligne ne lancera pas d'exception
            return true;
        } catch (Exception e) {
            // Ajoutez des logs ou des messages d'erreur plus détaillés pour faciliter le diagnostic
            return false; // Le token est invalide
        }
    }
}
