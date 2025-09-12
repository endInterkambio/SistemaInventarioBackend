package org.interkambio.SistemaInventarioBackend.DTO.sales;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReceivedDTO {
    private Long id;
    private Long saleOrderId; // FK hacia el pedido
    private String saleOrderNumber; // número legible de la orden
    private SaleOrderCustomerDTO customer; // cliente asociado
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private BigDecimal amount;
    private String referenceNumber;
}
