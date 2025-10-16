package org.interkambio.SistemaInventarioBackend.DTO.purchase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PurchaseOrderItemDTO {

    private Long id;
    private String bookTitle;
    private Long bookStockLocation;
    private Integer quantity;
    private BigDecimal discount;
    private BigDecimal customPrice;
    private BigDecimal offerPrice;
    private Boolean isOfferActive;
}
