package org.interkambio.SistemaInventarioBackend.criteria;

import lombok.Data;
import org.interkambio.SistemaInventarioBackend.model.OrderStatus;

import java.time.OffsetDateTime;

@Data
public class SaleOrderSearchCriteria {
    private String search;

    private String orderNumber;
    private String customerName;
    private String saleChannel;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private OrderStatus status;

    private String sortBy;
    private String sortDirection;
}
