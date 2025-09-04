package org.interkambio.SistemaInventarioBackend.DTO.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleIdNameDTO {
    private Long id;
    private String name;
}
