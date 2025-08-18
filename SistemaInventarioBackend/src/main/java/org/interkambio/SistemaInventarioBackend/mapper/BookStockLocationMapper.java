package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.BookStockLocationDTO;
import org.interkambio.SistemaInventarioBackend.DTO.SimpleIdNameDTO;
import org.interkambio.SistemaInventarioBackend.model.*;
import org.interkambio.SistemaInventarioBackend.repository.BookStockAdjustmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BookStockLocationMapper implements GenericMapper<BookStockLocation, BookStockLocationDTO> {

    private final BookStockAdjustmentRepository adjustmentRepository;

    @Autowired
    public BookStockLocationMapper(BookStockAdjustmentRepository adjustmentRepository) {
        this.adjustmentRepository = adjustmentRepository;
    }

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
        BookStockLocationDTO dto = new BookStockLocationDTO();

        dto.setId(entity.getId());
        dto.setBookSku(entity.getBook() != null ? entity.getBook().getSku() : null);

        if (entity.getWarehouse() != null) {
            dto.setWarehouse(new SimpleIdNameDTO(
                    entity.getWarehouse().getId(),
                    entity.getWarehouse().getName()
            ));
        }

        dto.setBookcase(entity.getBookcase());
        dto.setBookcaseFloor(entity.getBookcaseFloor());
        dto.setStock(entity.getStock());
        dto.setBookCondition(entity.getBookCondition() != null ? entity.getBookCondition().name() : null);
        dto.setLocationType(entity.getLocationType() != null ? entity.getLocationType().name() : null);
        dto.setLastUpdatedAt(entity.getLastUpdatedAt());

        dto.setLastStock(getLastStockValue(entity));

        return dto;
    }

    private Integer getLastStockValue(BookStockLocation entity) {
        if (entity.getId() == null) {
            return null; // si es nuevo no tiene histórico
        }

        return adjustmentRepository
                .findTopByLocationIdOrderByPerformedAtDesc(entity.getId())
                .map(adj -> entity.getStock() - adj.getAdjustmentQuantity())
                .orElse(null);
    }

}
