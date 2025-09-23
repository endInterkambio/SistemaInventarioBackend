package org.interkambio.SistemaInventarioBackend.DTO.sales;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.common.SimpleIdNameDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShipmentDTO {
    private Long id;
    private SimpleIdNameDTO order; // referencia a la orden
    private LocalDateTime shipmentDate;
    private String trackingNumber;
    private String address;
    private BigDecimal shippingFee;
    private SimpleIdNameDTO shipmentMethod;

    // Solo para mostrar los items heredados de la orden, opcional
    private List<SaleOrderItemDTO> items;
}
