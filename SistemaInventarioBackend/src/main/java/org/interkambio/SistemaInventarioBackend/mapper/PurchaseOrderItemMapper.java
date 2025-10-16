package org.interkambio.SistemaInventarioBackend.mapper;

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
        if (dto.getBookStockLocation() != null) {
            BookStockLocation bsl = new BookStockLocation();
            bsl.setId(dto.getBookStockLocation());
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

        // Relación BookStockLocation → DTO
        if (entity.getBookStockLocation() != null) {
            BookStockLocation bsl = entity.getBookStockLocation();
            dto.setBookStockLocation(bsl.getId());

            // Mapeo adicional del libro
            if (bsl.getBook() != null) {
                dto.setBookTitle(bsl.getBook().getTitle());

                // Precio de oferta (si aplica)
                if (Boolean.TRUE.equals(bsl.getBook().getIsOfferActive())) {
                    dto.setOfferPrice(bsl.getBook().getOfferPrice());
                } else {
                    dto.setOfferPrice(null);
                }

                dto.setIsOfferActive(bsl.getBook().getIsOfferActive());
            }
        }

        return dto;
    }
}
