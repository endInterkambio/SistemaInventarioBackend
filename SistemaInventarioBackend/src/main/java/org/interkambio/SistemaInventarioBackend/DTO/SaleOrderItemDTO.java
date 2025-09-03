package org.interkambio.SistemaInventarioBackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleOrderItemDTO {

    private Long id;

    // Solo referencia mínima al libro/stock
    private SimpleIdNameDTO bookStockLocation; // { id, name } o algún identificador del libro

    private Integer quantity;
    private BigDecimal discount;
    private BigDecimal customPrice;
}
