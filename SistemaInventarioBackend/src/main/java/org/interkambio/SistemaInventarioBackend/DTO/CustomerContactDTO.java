package org.interkambio.SistemaInventarioBackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerContactDTO {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;

    // Opcional: incluir referencia al cliente como solo su ID
    private Long customerId;
}

