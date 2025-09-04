package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.inventory.BookStockLocationDTO;
import org.interkambio.SistemaInventarioBackend.DTO.common.SimpleIdNameDTO;
import org.interkambio.SistemaInventarioBackend.model.*;
import org.interkambio.SistemaInventarioBackend.repository.InventoryTransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class BookStockLocationMapper implements GenericMapper<BookStockLocation, BookStockLocationDTO> {

    private final InventoryTransactionRepository transactionRepository;

    public BookStockLocationMapper(InventoryTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public BookStockLocation toEntity(BookStockLocationDTO dto) {
        BookStockLocation entity = new BookStockLocation();
        entity.setId(dto.getId());

        // Relación real por bookId
        if (dto.getBookId() != null) {
            Book book = new Book();
            book.setId(dto.getBookId());
            entity.setBook(book);
        }

        // Mantener compatibilidad con bookSku
        if (dto.getBookSku() != null) {
            entity.setBookSku(dto.getBookSku());
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

        return entity;
    }

    @Override
    public BookStockLocationDTO toDTO(BookStockLocation entity) {
        BookStockLocationDTO dto = new BookStockLocationDTO();

        dto.setId(entity.getId());
        dto.setBookId(entity.getBook() != null ? entity.getBook().getId() : null);
        dto.setBookSku(entity.getBookSku());

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

        dto.setLastStock(calculateLastStock(entity));

        return dto;
    }

    /**
     * Calcula el stock histórico antes del último ajuste.
     * No lanza error si no hay transacciones.
     */
    private Integer calculateLastStock(BookStockLocation location) {
        if (location.getId() == null) return null;

        Page<InventoryTransaction> page = transactionRepository.findRelevantTransactions(
                location.getId(),
                List.of(
                        TransactionType.ADJUSTMENT,
                        TransactionType.TRANSFER,
                        TransactionType.PURCHASE,
                        TransactionType.SALE,
                        TransactionType.RETURN_IN,
                        TransactionType.RETURN_OUT
                ),
                PageRequest.of(0, 1) // Tomamos solo la primera fila
        );

        if (page.isEmpty()) return null;

        InventoryTransaction lastTx = page.getContent().get(0);

        OffsetDateTime before = lastTx.getTransactionDate().minusNanos(1);

        Integer totalIn = transactionRepository.sumQuantityByToLocationBefore(location.getId(), before);
        Integer totalOut = transactionRepository.sumQuantityByFromLocationBefore(location.getId(), before);

        totalIn = totalIn != null ? totalIn : 0;
        totalOut = totalOut != null ? totalOut : 0;

        return totalIn - totalOut;
    }


}
