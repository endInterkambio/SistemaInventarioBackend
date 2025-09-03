package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.SaleOrderItemDTO;
import org.interkambio.SistemaInventarioBackend.DTO.SimpleIdNameDTO;
import org.interkambio.SistemaInventarioBackend.model.BookStockLocation;
import org.interkambio.SistemaInventarioBackend.model.SaleOrderItem;
import org.springframework.stereotype.Component;

@Component
public class SaleOrderItemMapper implements GenericMapper<SaleOrderItem, SaleOrderItemDTO> {

    @Override
    public SaleOrderItem toEntity(SaleOrderItemDTO dto) {
        SaleOrderItem item = new SaleOrderItem();
        item.setId(dto.getId());
        item.setQuantity(dto.getQuantity());
        item.setDiscount(dto.getDiscount());
        item.setCustomPrice(dto.getCustomPrice());

        if (dto.getBookStockLocation() != null && dto.getBookStockLocation().getId() != null) {
            BookStockLocation bsl = new BookStockLocation();
            bsl.setId(dto.getBookStockLocation().getId());
            item.setBookStockLocation(bsl);
        }

        return item;
    }

    @Override
    public SaleOrderItemDTO toDTO(SaleOrderItem entity) {
        SaleOrderItemDTO dto = new SaleOrderItemDTO();
        dto.setId(entity.getId());
        dto.setQuantity(entity.getQuantity());
        dto.setDiscount(entity.getDiscount());
        dto.setCustomPrice(entity.getCustomPrice());

        if (entity.getBookStockLocation() != null) {
            dto.setBookStockLocation(new SimpleIdNameDTO(
                    entity.getBookStockLocation().getId(),
                    entity.getBookStockLocation().getBook().getTitle()
            ));
        }

        return dto;
    }
}

