package org.interkambio.SistemaInventarioBackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserPreferenceDTO {

    private long id;
    private String preferenceKey;
    private String preferenceValue;
    private Long userId;
}
