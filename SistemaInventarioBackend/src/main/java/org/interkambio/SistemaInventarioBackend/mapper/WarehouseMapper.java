package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.WarehouseDTO;
import org.interkambio.SistemaInventarioBackend.model.BookStockLocation;
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

        // Calcular totalBooks sumando stock de cada BookStockLocation
        /*if (entity.getStockLocations() != null) {
            int total = entity.getStockLocations()
                    .stream()
                    .mapToInt(BookStockLocation::getStock)
                    .sum();
            dto.setTotalBooks(total);
        } else {
            dto.setTotalBooks(0);
        }*/

        return dto;
    }
}
