package org.interkambio.SistemaInventarioBackend.DTO.sales;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleOrderCustomerDTO {
    private Long id;
    private String name;
    private String companyName; // Opcional, solo para empresas
    private String customerType; // "PERSON" o "COMPANY"
}

