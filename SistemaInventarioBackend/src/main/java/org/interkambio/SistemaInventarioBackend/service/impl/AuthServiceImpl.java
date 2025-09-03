package org.interkambio.SistemaInventarioBackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.RoleDTO;
import org.interkambio.SistemaInventarioBackend.DTO.UserDTO;
import org.interkambio.SistemaInventarioBackend.DTO.login.LoginRequestDTO;
import org.interkambio.SistemaInventarioBackend.DTO.login.LoginResponseDTO;
import org.interkambio.SistemaInventarioBackend.model.User;
import org.interkambio.SistemaInventarioBackend.repository.UserRepository;
import org.interkambio.SistemaInventarioBackend.security.JwtProvider;
import org.interkambio.SistemaInventarioBackend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        // 1. Buscar usuario
        User user = userRepository.findByUsernameCaseSensitive(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Validar contraseña
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        // 3. Generar tokens
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        // 4. Retornar respuesta
        return new LoginResponseDTO(
                accessToken,
                refreshToken,
                new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        null,
                        user.getRole() != null
                                ? new RoleDTO(user.getRole().getId(), user.getRole().getName())
                                : null
                )
        );
    }

}

