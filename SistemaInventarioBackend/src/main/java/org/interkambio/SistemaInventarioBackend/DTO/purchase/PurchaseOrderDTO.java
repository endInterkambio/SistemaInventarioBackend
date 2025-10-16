package org.interkambio.SistemaInventarioBackend.DTO.purchase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.common.SimpleIdNameDTO;
import org.interkambio.SistemaInventarioBackend.model.OrderStatus;
import org.interkambio.SistemaInventarioBackend.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PurchaseOrderDTO {

    private Long id;

    private String purchaseOrderNumber;

    private LocalDateTime purchaseDate;

    private Long supplierId; // Relación con Supplier

    private String supplierName; // Campo adicional para mostrar nombre del proveedor

    private LocalDateTime createdAt;

    private SimpleIdNameDTO createdById;

    private String purchaseChannel;

    private BigDecimal amount;

    private BigDecimal amountShipment;

    private BigDecimal additionalFee;

    private BigDecimal totalPaid; // suma de todos los PaymentMade asociados

    private BigDecimal totalAmount; // Subtotal + envío + fee

    private OrderStatus status;

    private PaymentStatus paymentStatus;

    private String supplierNotes;

    private LocalDateTime deliveryDate;

    private List<PurchaseOrderItemDTO> items; // Lista de ítems asociados
}
