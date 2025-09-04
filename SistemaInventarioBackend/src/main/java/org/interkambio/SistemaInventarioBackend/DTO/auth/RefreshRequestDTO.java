package org.interkambio.SistemaInventarioBackend.DTO.auth;

import lombok.Data;

@Data
public class RefreshRequestDTO {
    private String refreshToken;
}
