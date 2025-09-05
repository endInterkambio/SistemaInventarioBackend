package org.interkambio.SistemaInventarioBackend.DTO.sales;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ShipmentMethodDTO {

    private Long id;
    private String name;
    private String description;
}