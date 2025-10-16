package org.interkambio.SistemaInventarioBackend.criteria;

import lombok.Data;
import org.interkambio.SistemaInventarioBackend.model.OrderStatus;

import java.time.LocalDateTime;

@Data
public class PurchaseOrderSearchCriteria {
    private String search;

    private String purchaseOrderNumber;
    private String supplierName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private OrderStatus status;

    private String sortBy;
    private String sortDirection;
}
