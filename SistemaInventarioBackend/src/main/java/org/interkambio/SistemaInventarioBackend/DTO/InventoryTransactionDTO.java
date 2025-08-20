package org.interkambio.SistemaInventarioBackend.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InventoryTransactionDTO {
    private Long id;
    private LocalDateTime transactionDate;
    private String bookSku;
    private Long fromLocationId;
    private Long toLocationId;
    private String transactionType;
    private Integer quantity;
    private String reason;
    private Long userId;
    private LocalDateTime createdAt;
}
