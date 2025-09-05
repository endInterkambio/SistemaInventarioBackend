package org.interkambio.SistemaInventarioBackend.criteria;

import lombok.Data;
import org.interkambio.SistemaInventarioBackend.model.SaleOrderStatus;

import java.time.OffsetDateTime;

@Data
public class SaleOrderSearchCriteria {

    private String orderNumber;
    private String customerName;
    private String saleChannel;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private SaleOrderStatus status;
}
