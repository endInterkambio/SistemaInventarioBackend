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

    private Long purchaseOrderId; // Referencia al ID de la orden de compra

    private Long bookStockLocationId; // Referencia al ID de la ubicación de stock

    private Integer quantity;

    private BigDecimal customPrice;
}
