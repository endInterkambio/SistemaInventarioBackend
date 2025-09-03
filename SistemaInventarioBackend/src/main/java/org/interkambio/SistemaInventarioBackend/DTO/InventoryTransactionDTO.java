package org.interkambio.SistemaInventarioBackend.DTO;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class InventoryTransactionDTO {
    private Long id;
    private OffsetDateTime transactionDate;
    private String bookSku;
    private Long fromLocationId;
    private Long toLocationId;
    private String transactionType;
    private Integer quantity;
    private String reason;
    private Long userId;
    private OffsetDateTime createdAt;
}
