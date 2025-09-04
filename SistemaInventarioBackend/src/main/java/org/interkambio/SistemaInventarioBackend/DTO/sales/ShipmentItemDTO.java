package org.interkambio.SistemaInventarioBackend.DTO.sales;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.common.SimpleIdNameDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentItemDTO {

    private Long id;
    private SimpleIdNameDTO bookStockLocation; // Solo id + name para el frontend
    private Integer quantity;
}
