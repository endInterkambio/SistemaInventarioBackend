package org.interkambio.SistemaInventarioBackend.DTO.purchase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PaymentMadeDTO {
    private Long id;
    private Long purchaseOrderId;
    private LocalDateTime paymentDate;
    private BigDecimal amount;
    private String referenceNumber;
}
