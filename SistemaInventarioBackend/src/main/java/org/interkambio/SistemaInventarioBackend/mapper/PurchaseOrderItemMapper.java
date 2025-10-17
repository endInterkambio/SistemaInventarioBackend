package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.common.SimpleIdNameDTO;
import org.interkambio.SistemaInventarioBackend.DTO.inventory.BookStockLocationDTO;
import org.interkambio.SistemaInventarioBackend.DTO.purchase.PurchaseOrderItemDTO;
import org.interkambio.SistemaInventarioBackend.model.BookStockLocation;
import org.interkambio.SistemaInventarioBackend.model.PurchaseOrderItem;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderItemMapper implements GenericMapper<PurchaseOrderItem, PurchaseOrderItemDTO> {

    @Override
    public PurchaseOrderItem toEntity(PurchaseOrderItemDTO dto) {
        if (dto == null) return null;

        PurchaseOrderItem item = new PurchaseOrderItem();
        item.setId(dto.getId());
        item.setQuantity(dto.getQuantity());
        item.setDiscount(dto.getDiscount());
        item.setCustomPrice(dto.getCustomPrice());

        // Relación con BookStockLocation
        if (dto.getBookStockLocation() != null && dto.getBookStockLocation().getId() != null) {
            BookStockLocation bsl = new BookStockLocation();
            bsl.setId(dto.getBookStockLocation().getId());
            item.setBookStockLocation(bsl);
        }

        // La relación con PurchaseOrder se establece en el mapper padre (PurchaseOrderMapper)
        return item;
    }

    @Override
    public PurchaseOrderItemDTO toDTO(PurchaseOrderItem entity) {
        if (entity == null) return null;

        PurchaseOrderItemDTO dto = new PurchaseOrderItemDTO();
        dto.setId(entity.getId());
        dto.setQuantity(entity.getQuantity());
        dto.setDiscount(entity.getDiscount());
        dto.setCustomPrice(entity.getCustomPrice());

        // Mostrar precio de oferta dento del objeto solo si hay oferta activa
        if (entity.getBookStockLocation() != null
                && entity.getBookStockLocation().getBook() != null
                && Boolean.TRUE.equals(entity.getBookStockLocation().getBook().getIsOfferActive())) {
            dto.setOfferPrice(entity.getBookStockLocation().getBook().getOfferPrice());
        } else {
            dto.setOfferPrice(null);
        }

        dto.setIsOfferActive(entity.getBookStockLocation().getBook().getIsOfferActive());

        if (entity.getBookStockLocation() != null) {
            BookStockLocation bsl = entity.getBookStockLocation();

            // Mapear solo los campos útiles
            dto.setBookStockLocation(new BookStockLocationDTO(
                    bsl.getId(),
                    bsl.getBook().getId(),
                    bsl.getBook().getSku(),
                    new SimpleIdNameDTO(bsl.getWarehouse().getId(), bsl.getWarehouse().getName()),
                    bsl.getBookcase(),
                    bsl.getBookcaseFloor(),
                    bsl.getBookCondition().name(),
                    bsl.getLocationType().name(),
                    bsl.getStock()
            ));

            if (bsl.getBook() != null) {
                dto.setBookTitle(bsl.getBook().getTitle());
            }
        }

        return dto;
    }
}
