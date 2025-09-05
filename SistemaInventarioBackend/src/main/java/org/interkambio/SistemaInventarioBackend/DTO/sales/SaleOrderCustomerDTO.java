package org.interkambio.SistemaInventarioBackend.DTO.sales;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SaleOrderCustomerDTO {
    private Long id;
    private String name;
    private String companyName; // Opcional, solo para empresas
    private String customerType; // "PERSON" o "COMPANY"
}

