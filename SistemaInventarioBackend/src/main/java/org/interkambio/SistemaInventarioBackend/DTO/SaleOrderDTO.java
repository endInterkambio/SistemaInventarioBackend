package org.interkambio.SistemaInventarioBackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SaleOrderDTO {

    private Long id;
    private String orderNumber;
    private OffsetDateTime orderDate;
    private OffsetDateTime createdAt;
    private SimpleIdNameDTO createdBy;
    private String saleChannel;
    private BigDecimal amount;
    private BigDecimal amountShipment;
    private BigDecimal additionalFee;
    private SimpleIdNameDTO customer;
    private List<SaleOrderItemDTO> items;
}
