package org.interkambio.SistemaInventarioBackend.DTO.purchase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.common.SimpleIdNameDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PaymentMadeDTO {
    private Long id;
    private Long purchaseOrderId;
    private String purchaseOrderNumber;
    private SimpleIdNameDTO supplier; // Proveedor asociado
    private LocalDateTime paymentDate;
    private BigDecimal amount;
    private String referenceNumber;
    private String paymentMethod;
}
