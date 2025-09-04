package org.interkambio.SistemaInventarioBackend.controller;

import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.auth.LoginRequestDTO;
import org.interkambio.SistemaInventarioBackend.DTO.auth.LoginResponseDTO;
import org.interkambio.SistemaInventarioBackend.DTO.auth.RefreshRequestDTO;
import org.interkambio.SistemaInventarioBackend.DTO.auth.RefreshResponseDTO;
import org.interkambio.SistemaInventarioBackend.security.JwtProvider;
import org.interkambio.SistemaInventarioBackend.service.AuthService;
import org.interkambio.SistemaInventarioBackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequestDTO request) {
        String refreshToken = request.getRefreshToken();

        if (jwtProvider.validateToken(refreshToken)) {
            String username = jwtProvider.getUsernameFromToken(refreshToken);
            Long userId = jwtProvider.getUserIdFromToken(refreshToken);
            String role = jwtProvider.getRoleFromToken(refreshToken); // si lo incluyes también en refresh

            String newAccessToken = jwtProvider.generateAccessToken(userId, username, role);

            return ResponseEntity.ok(new RefreshResponseDTO(newAccessToken));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Refresh token inválido o expirado"));
    }

}
