package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.inventory.WarehouseDTO;
import org.interkambio.SistemaInventarioBackend.model.Warehouse;
import org.springframework.stereotype.Component;

@Component
public class WarehouseMapper implements GenericMapper<Warehouse, WarehouseDTO> {

    @Override
    public Warehouse toEntity(WarehouseDTO dto) {
        if (dto == null) {
            return null;
        }

        Warehouse entity = new Warehouse();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setLocation(dto.getLocation());
        entity.setDescription(dto.getDescription());

        return entity;
    }

    @Override
    public WarehouseDTO toDTO(Warehouse entity) {
        if (entity == null) {
            return null;
        }

        WarehouseDTO dto = new WarehouseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setLocation(entity.getLocation());
        dto.setDescription(entity.getDescription());

        return dto;
    }
}
