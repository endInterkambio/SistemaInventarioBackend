package org.interkambio.SistemaInventarioBackend.criteria;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentReceivedCriteria {
    private Long saleOrderId;
    private String paymentMethod;
    private LocalDateTime paymentDateFrom;
    private LocalDateTime paymentDateTo;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private String referenceNumber;
}
