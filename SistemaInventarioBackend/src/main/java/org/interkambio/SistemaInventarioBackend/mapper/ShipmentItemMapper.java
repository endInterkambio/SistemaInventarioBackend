package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.common.SimpleIdNameDTO;
import org.interkambio.SistemaInventarioBackend.DTO.sales.ShipmentItemDTO;
import org.interkambio.SistemaInventarioBackend.model.BookStockLocation;
import org.interkambio.SistemaInventarioBackend.model.ShipmentItem;
import org.springframework.stereotype.Component;

@Component
public class ShipmentItemMapper implements GenericMapper<ShipmentItem, ShipmentItemDTO> {

    @Override
    public ShipmentItem toEntity(ShipmentItemDTO dto) {
        ShipmentItem item = new ShipmentItem();
        item.setId(dto.getId());
        item.setQuantity(dto.getQuantity());

        if (dto.getBookStockLocation() != null) {
            BookStockLocation location = new BookStockLocation();
            location.setId(dto.getBookStockLocation().getId());
            item.setBookStockLocation(location);
        }

        return item;
    }

    @Override
    public ShipmentItemDTO toDTO(ShipmentItem entity) {
        ShipmentItemDTO dto = new ShipmentItemDTO();
        dto.setId(entity.getId());
        dto.setQuantity(entity.getQuantity());

        if (entity.getBookStockLocation() != null) {
            dto.setBookStockLocation(new SimpleIdNameDTO(
                    entity.getBookStockLocation().getId(),
                    entity.getBookStockLocation().getDisplayName()
            ));
        }

        return dto;
    }
}
