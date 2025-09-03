package org.interkambio.SistemaInventarioBackend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.interkambio.SistemaInventarioBackend.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    private final Algorithm algorithm;
    private final long accessExpiration;
    private final long refreshExpiration;

    public JwtProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access.expiration}") long accessExpiration,
            @Value("${jwt.refresh.expiration}") long refreshExpiration
    ) {
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    // Generar Access Token con entidad User
    public String generateAccessToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("userId", user.getId())
                .withClaim("role", user.getRole().getName())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessExpiration))
                .sign(algorithm);
    }

    // Generar Refresh Token con entidad User
    public String generateRefreshToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("userId", user.getId())
                .withClaim("role", user.getRole().getName())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpiration))
                .sign(algorithm);
    }

    // Overloads para cuando no tengas entidad completa
    public String generateAccessToken(Long userId, String username, String roleName) {
        return JWT.create()
                .withSubject(username)
                .withClaim("userId", userId)
                .withClaim("role", roleName)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessExpiration))
                .sign(algorithm);
    }

    public String generateRefreshToken(Long userId, String username, String roleName) {
        return JWT.create()
                .withSubject(username)
                .withClaim("userId", userId)
                .withClaim("role", roleName)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpiration))
                .sign(algorithm);
    }

    // Validar cualquier token
    public boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Extraer claims
    public Long getUserIdFromToken(String token) {
        return JWT.decode(token).getClaim("userId").asLong();
    }

    public String getUsernameFromToken(String token) {
        return JWT.decode(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return JWT.decode(token).getClaim("role").asString();
    }
}
