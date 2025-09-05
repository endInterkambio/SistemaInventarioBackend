package org.interkambio.SistemaInventarioBackend.DTO.sales;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentDTO {
    private Long id;
    private Long orderId; // referencia a la orden
    private LocalDateTime shipmentDate;
    private String trackingNumber;
    private String address;
    private BigDecimal shippingFee;
    private ShipmentMethodDTO shipmentMethod;

    // Solo para mostrar los items heredados de la orden, opcional
    private List<SaleOrderItemDTO> items;
}
