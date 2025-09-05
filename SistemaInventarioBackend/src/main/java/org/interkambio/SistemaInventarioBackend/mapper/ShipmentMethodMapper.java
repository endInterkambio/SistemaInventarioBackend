package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.sales.ShipmentMethodDTO;
import org.interkambio.SistemaInventarioBackend.model.ShipmentMethod;
import org.springframework.stereotype.Component;

@Component
public class ShipmentMethodMapper implements GenericMapper<ShipmentMethod, ShipmentMethodDTO> {

    @Override
    public ShipmentMethod toEntity(ShipmentMethodDTO dto) {
        if (dto == null) return null;
        ShipmentMethod method = new ShipmentMethod();
        method.setId(dto.getId());
        method.setName(dto.getName());
        method.setDescription(dto.getDescription());
        return method;
    }

    @Override
    public ShipmentMethodDTO toDTO(ShipmentMethod entity) {
        if (entity == null) return null;
        ShipmentMethodDTO dto = new ShipmentMethodDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }
}
