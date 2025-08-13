package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.BookStockLocationDTO;
import org.interkambio.SistemaInventarioBackend.DTO.SimpleIdNameDTO;
import org.interkambio.SistemaInventarioBackend.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BookStockLocationMapper implements GenericMapper<BookStockLocation, BookStockLocationDTO> {

    @Override
    public BookStockLocation toEntity(BookStockLocationDTO dto) {
        BookStockLocation entity = new BookStockLocation();
        entity.setId(dto.getId());

        if (dto.getBookSku() != null) {
            Book book = new Book();
            book.setSku(dto.getBookSku());
            entity.setBook(book);
        }

        if (dto.getWarehouse() != null && dto.getWarehouse().getId() != null) {
            Warehouse warehouse = new Warehouse();
            warehouse.setId(dto.getWarehouse().getId());
            entity.setWarehouse(warehouse);
        }

        entity.setBookcase(dto.getBookcase());
        entity.setBookcaseFloor(dto.getBookcaseFloor());
        entity.setStock(dto.getStock());

        if (dto.getBookCondition() != null) {
            entity.setBookCondition(BookCondition.valueOf(dto.getBookCondition()));
        }

        if (dto.getLocationType() != null) {
            entity.setLocationType(LocationType.valueOf(dto.getLocationType()));
        }

        if (dto.getLastUpdatedAt() != null) {
            entity.setLastUpdatedAt(LocalDateTime.now());
        }

        return entity;
    }

    @Override
    public BookStockLocationDTO toDTO(BookStockLocation entity) {
        return new BookStockLocationDTO(
                entity.getId(),
                entity.getBook() != null ? entity.getBook().getSku() : null,
                entity.getWarehouse() != null
                        ? new SimpleIdNameDTO(entity.getWarehouse().getId(), entity.getWarehouse().getName())
                        : null,
                entity.getBookcase(),
                entity.getBookcaseFloor(),
                entity.getStock(),
                entity.getBookCondition() != null ? entity.getBookCondition().name() : null,
                entity.getLocationType() != null ? entity.getLocationType().name() : null,
                entity.getLastUpdatedAt()
        );
    }
}
