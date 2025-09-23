package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.common.SimpleIdNameDTO;
import org.interkambio.SistemaInventarioBackend.DTO.sales.ShipmentDTO;
import org.interkambio.SistemaInventarioBackend.DTO.sales.SaleOrderItemDTO;
import org.interkambio.SistemaInventarioBackend.model.Shipment;
import org.interkambio.SistemaInventarioBackend.model.ShipmentMethod;
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
        if (dto.getShipmentMethod() != null && dto.getShipmentMethod().getId() != null) {
            ShipmentMethod shipmentMethod = new ShipmentMethod();
            shipmentMethod.setId(dto.getShipmentMethod().getId());
            shipment.setShipmentMethod(shipmentMethod);
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
            dto.setShipmentMethod(new SimpleIdNameDTO(
                    entity.getShipmentMethod().getId(),
                    entity.getShipmentMethod().getName()
            ));
        }

        // Asignar orderId
        if (entity.getOrder() != null) {
            dto.setOrder(new SimpleIdNameDTO(entity.getOrder().getId(), entity.getOrder().getOrderNumber()));

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
