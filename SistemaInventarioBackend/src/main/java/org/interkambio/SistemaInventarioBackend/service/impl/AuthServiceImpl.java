package org.interkambio.SistemaInventarioBackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.UserDTO;
import org.interkambio.SistemaInventarioBackend.DTO.login.LoginRequestDTO;
import org.interkambio.SistemaInventarioBackend.DTO.login.LoginResponseDTO;
import org.interkambio.SistemaInventarioBackend.model.User;
import org.interkambio.SistemaInventarioBackend.repository.UserRepository;
import org.interkambio.SistemaInventarioBackend.security.JwtProvider;
import org.interkambio.SistemaInventarioBackend.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        // 2. Validar contraseña con BCrypt
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // 3. Generar token con JWT
        String token = jwtProvider.generateToken(user.getId(), user.getUsername());

        // 4. Retornar respuesta
        return new LoginResponseDTO(
                token,
                new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        null,
                        user.getRole().getId()
                )
        );
    }
}

