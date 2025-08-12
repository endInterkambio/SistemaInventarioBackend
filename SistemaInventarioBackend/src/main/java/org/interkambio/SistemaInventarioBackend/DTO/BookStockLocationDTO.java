package org.interkambio.SistemaInventarioBackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookStockLocationDTO {
    private Long id;
    private String bookSku;
    private SimpleIdNameDTO warehouse; // en lugar de warehouseId
    private Integer bookcase;
    private Integer bookcaseFloor;
    private int stock;
    private String bookCondition;
    private String locationType;
}


