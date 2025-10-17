package org.interkambio.SistemaInventarioBackend.criteria;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentMadeCriteria {
    private String search;

    private Long purchaseOrderId;
    private String purchaseOrderNumber;
    private String paymentMethod;
    private LocalDateTime paymentDateFrom;
    private LocalDateTime paymentDateTo;
    private String supplierName;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private String referenceNumber;
}
