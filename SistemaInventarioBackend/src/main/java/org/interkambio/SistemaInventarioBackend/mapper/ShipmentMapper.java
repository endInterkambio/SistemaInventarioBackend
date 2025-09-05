package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.sales.ShipmentDTO;
import org.interkambio.SistemaInventarioBackend.DTO.sales.SaleOrderItemDTO;
import org.interkambio.SistemaInventarioBackend.model.Shipment;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ShipmentMapper implements GenericMapper<Shipment, ShipmentDTO> {

    private final ShipmentMethodMapper methodMapper;
    private final SaleOrderItemMapper saleOrderItemMapper;

    public ShipmentMapper(ShipmentMethodMapper methodMapper, SaleOrderItemMapper saleOrderItemMapper) {
        this.methodMapper = methodMapper;
        this.saleOrderItemMapper = saleOrderItemMapper;
    }

    @Override
    public Shipment toEntity(ShipmentDTO dto) {
        Shipment shipment = new Shipment();
        shipment.setId(dto.getId());
        shipment.setShipmentDate(dto.getShipmentDate());
        shipment.setTrackingNumber(dto.getTrackingNumber());
        shipment.setAddress(dto.getAddress());
        shipment.setShippingFee(dto.getShippingFee());

        // Asignar ShipmentMethod
        if (dto.getShipmentMethod() != null) {
            shipment.setShipmentMethod(methodMapper.toEntity(dto.getShipmentMethod()));
        }

        // Asignar SaleOrder si viene orderId
        if (dto.getOrderId() != null) {
            var order = new org.interkambio.SistemaInventarioBackend.model.SaleOrder();
            order.setId(dto.getOrderId());
            shipment.setOrder(order);
        }

        return shipment;
    }

    @Override
    public ShipmentDTO toDTO(Shipment entity) {
        ShipmentDTO dto = new ShipmentDTO();
        dto.setId(entity.getId());
        dto.setShipmentDate(entity.getShipmentDate());
        dto.setTrackingNumber(entity.getTrackingNumber());
        dto.setAddress(entity.getAddress());
        dto.setShippingFee(entity.getShippingFee());

        // Mapear ShipmentMethod
        if (entity.getShipmentMethod() != null) {
            dto.setShipmentMethod(methodMapper.toDTO(entity.getShipmentMethod()));
        }

        // Asignar orderId
        if (entity.getOrder() != null) {
            dto.setOrderId(entity.getOrder().getId());

            // Opcional: mapear items de la orden para visualización
            if (entity.getOrder() != null && entity.getOrder().getItems() != null) {
                dto.setItems(
                        entity.getOrder().getItems().stream()
                                .map(saleOrderItemMapper::toDTO)
                                .collect(Collectors.toList())
                );
            }
        }

        return dto;
    }
}
