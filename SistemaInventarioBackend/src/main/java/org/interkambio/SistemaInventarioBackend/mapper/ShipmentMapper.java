package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.sales.ShipmentDTO;
import org.interkambio.SistemaInventarioBackend.model.Shipment;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ShipmentMapper implements GenericMapper<Shipment, ShipmentDTO> {

    private final ShipmentItemMapper itemMapper;

    public ShipmentMapper(ShipmentItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    @Override
    public Shipment toEntity(ShipmentDTO dto) {
        Shipment shipment = new Shipment();
        shipment.setId(dto.getId());
        shipment.setShipmentDate(dto.getShipmentDate());
        shipment.setTrackingNumber(dto.getTrackingNumber());
        shipment.setAddress(dto.getAddress());
        shipment.setShippingFee(dto.getShippingFee());

        if (dto.getItems() != null) {
            shipment.setItems(dto.getItems().stream()
                    .map(itemDto -> {
                        var item = itemMapper.toEntity(itemDto);
                        item.setShipment(shipment); // relación bidireccional
                        return item;
                    })
                    .collect(Collectors.toList()));
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

        if (entity.getItems() != null) {
            dto.setItems(entity.getItems().stream()
                    .map(itemMapper::toDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
