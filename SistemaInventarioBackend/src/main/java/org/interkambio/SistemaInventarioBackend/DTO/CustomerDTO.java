package org.interkambio.SistemaInventarioBackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CustomerDTO {

    private Long id;
    private String customerType;
    private String documentType;
    private String documentNumber;
    private String name;
    private String companyName;
    private String email;
    private String phoneNumber;
    private String address;
    private List<CustomerContactDTO> contacts;
}
