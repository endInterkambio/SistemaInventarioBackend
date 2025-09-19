package org.interkambio.SistemaInventarioBackend.criteria;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ShipmentSearchCriteria {

    private String search;

    private String trackingNumber;
    private String address;
    private LocalDateTime shipmentDateFrom;
    private LocalDateTime shipmentDateTo;
    private BigDecimal minShippingFee;
    private BigDecimal maxShippingFee;
    private Long orderId; // Para buscar envíos asociados a una orden específica
}
