package org.interkambio.SistemaInventarioBackend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    private final String SECRET_KEY = "mi_clave_secreta_super_segura"; // cámbiala por algo fuerte
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hora

    private final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

    // Generar un token JWT con ID y username
    public String generateToken(Long userId, String username) {
        return JWT.create()
                .withSubject(username)
                .withClaim("userId", userId)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }


    // Validar token y retornar true/false
    public boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Extraer userId desde el token
    public Long getUserIdFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim("userId").asLong();
    }

    // Extraer username desde el token
    public String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getSubject();
    }
}
