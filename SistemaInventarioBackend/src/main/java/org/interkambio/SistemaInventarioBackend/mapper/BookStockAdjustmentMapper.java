package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.BookStockAdjustmentDTO;
import org.interkambio.SistemaInventarioBackend.DTO.SimpleIdNameDTO;
import org.interkambio.SistemaInventarioBackend.model.Book;
import org.interkambio.SistemaInventarioBackend.model.BookStockAdjustment;
import org.interkambio.SistemaInventarioBackend.model.BookStockLocation;
import org.interkambio.SistemaInventarioBackend.model.User;
import org.springframework.stereotype.Component;

@Component
public class BookStockAdjustmentMapper implements GenericMapper<BookStockAdjustment, BookStockAdjustmentDTO> {

    @Override
    public BookStockAdjustment toEntity(BookStockAdjustmentDTO dto) {
        BookStockAdjustment adj = new BookStockAdjustment();
        adj.setId(dto.getId());

        if (dto.getBook() != null && dto.getBook().getId() != null) {
            Book book = new Book();
            book.setId(dto.getBook().getId());
            adj.setBook(book);
        }

        if (dto.getLocation() != null && dto.getLocation().getId() != null) {
            BookStockLocation loc = new BookStockLocation();
            loc.setId(dto.getLocation().getId());
            adj.setLocation(loc);
        }

        adj.setAdjustmentQuantity(dto.getAdjustmentQuantity());
        adj.setReason(dto.getReason());

        if (dto.getPerformedBy() != null && dto.getPerformedBy().getId() != null) {
            User user = new User();
            user.setId(dto.getPerformedBy().getId());
            adj.setPerformedBy(user);
        }

        adj.setPerformedAt(dto.getPerformedAt());

        return adj;
    }

    @Override
    public BookStockAdjustmentDTO toDTO(BookStockAdjustment entity) {
        return new BookStockAdjustmentDTO(
                entity.getId(),
                entity.getBook() != null ? new SimpleIdNameDTO(entity.getBook().getId(), entity.getBook().getTitle()) : null,
                entity.getLocation() != null ? new SimpleIdNameDTO(entity.getLocation().getId(), entity.getLocation().getDisplayName()) : null,
                entity.getAdjustmentQuantity(),
                entity.getReason(),
                entity.getPerformedBy() != null ? new SimpleIdNameDTO(entity.getPerformedBy().getId(), entity.getPerformedBy().getUsername()) : null,
                entity.getPerformedAt()
        );
    }
}

