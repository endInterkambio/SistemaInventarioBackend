package org.interkambio.SistemaInventarioBackend.DTO.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshResponseDTO {
    private String accessToken;
}

