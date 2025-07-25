package org.interkambio.SistemaInventarioBackend.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class UserDTO {

    private Long id;
    private String username;

    @JsonIgnoreProperties("password")
    private String password;

    private Long roleId;
}
