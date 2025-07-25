package org.interkambio.SistemaInventarioBackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class UserPreferenceDTO {

    private long id;
    private String preferenceKey;
    private String preferenceValue;
    private Long userId;
}
