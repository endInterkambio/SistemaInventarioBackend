package org.interkambio.SistemaInventarioBackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookStockLocationDTO {
    private Long id;
    private String bookSku;  // Solo el SKU, no el objeto Book completo
    private Long warehouseId;
    private Integer bookcase;
    private Integer bookcaseFloor;
    private int stock;
    private String bookCondition;
}

