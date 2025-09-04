package org.interkambio.SistemaInventarioBackend.DTO.sales;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.common.SimpleIdNameDTO;

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
    private SaleOrderCustomerDTO customer;
    private List<SaleOrderItemDTO> items;
}
