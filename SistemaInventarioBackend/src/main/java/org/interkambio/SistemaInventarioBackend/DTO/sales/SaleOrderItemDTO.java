package org.interkambio.SistemaInventarioBackend.DTO.sales;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.inventory.BookStockLocationDTO;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SaleOrderItemDTO {

    private Long id;
    private String bookTitle;
    private BookStockLocationDTO bookStockLocation;
    private Integer quantity;
    private BigDecimal discount;
    private BigDecimal customPrice;
    private BigDecimal offerPrice;
    private Boolean isOfferActive;
}
