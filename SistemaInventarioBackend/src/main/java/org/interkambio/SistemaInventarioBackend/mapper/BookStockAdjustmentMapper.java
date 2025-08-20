package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.BookStockAdjustmentDTO;
import org.interkambio.SistemaInventarioBackend.DTO.SimpleIdNameDTO;
import org.interkambio.SistemaInventarioBackend.model.Book;
import org.interkambio.SistemaInventarioBackend.model.BookStockAdjustment;
import org.interkambio.SistemaInventarioBackend.model.BookStockLocation;
import org.interkambio.SistemaInventarioBackend.model.User;
import org.interkambio.SistemaInventarioBackend.repository.BookRepository;
import org.interkambio.SistemaInventarioBackend.repository.BookStockLocationRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BookStockAdjustmentMapper implements GenericMapper<BookStockAdjustment, BookStockAdjustmentDTO> {

    private final BookRepository bookRepository;
    private final BookStockLocationRepository locationRepository;

    public BookStockAdjustmentMapper(BookRepository bookRepository,
                                     BookStockLocationRepository locationRepository) {
        this.bookRepository = bookRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public BookStockAdjustment toEntity(BookStockAdjustmentDTO dto) {
        BookStockAdjustment adj = new BookStockAdjustment();
        adj.setId(dto.getId());

        // Buscar el libro por SKU
        if (dto.getBookSku() != null) {
            Book book = bookRepository.findBySku(dto.getBookSku())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Book with SKU " + dto.getBookSku() + " not found"));
            adj.setBook(book);
        }

        // Buscar la ubicación por ID
        if (dto.getLocationId() != null) {
            BookStockLocation location = locationRepository.findById(dto.getLocationId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Location with ID " + dto.getLocationId() + " not found"));
            adj.setLocation(location);
        }

        adj.setAdjustmentQuantity(dto.getAdjustmentQuantity());
        adj.setReason(dto.getReason());

        // Asignar usuario (solo con ID)
        if (dto.getPerformedBy() != null && dto.getPerformedBy().getId() != null) {
            User user = new User();
            user.setId(dto.getPerformedBy().getId());
            adj.setPerformedBy(user);
        }

        adj.setPerformedAt(dto.getPerformedAt() != null ? dto.getPerformedAt() : LocalDateTime.now());

        return adj;
    }

    @Override
    public BookStockAdjustmentDTO toDTO(BookStockAdjustment entity) {
        return new BookStockAdjustmentDTO(
                entity.getId(),
                entity.getBook() != null ? entity.getBook().getSku() : null,
                entity.getLocation() != null ? entity.getLocation().getId() : null,
                entity.getAdjustmentQuantity(),
                entity.getReason(),
                entity.getPerformedBy() != null
                        ? new SimpleIdNameDTO(entity.getPerformedBy().getId(), entity.getPerformedBy().getUsername())
                        : null,
                entity.getPerformedAt()
        );
    }
}
