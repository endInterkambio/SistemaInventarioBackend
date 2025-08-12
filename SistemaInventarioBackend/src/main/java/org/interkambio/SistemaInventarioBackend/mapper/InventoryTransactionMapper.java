package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.InventoryTransactionDTO;
import org.interkambio.SistemaInventarioBackend.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InventoryTransactionMapper {

    public InventoryTransactionDTO toDTO(InventoryTransaction transaction) {
        if (transaction == null) return null;

        InventoryTransactionDTO dto = new InventoryTransactionDTO();
        dto.setId(transaction.getId());
        dto.setTransactionDate(transaction.getTransactionDate());

        if (transaction.getBook() != null) {
            dto.setBookSku(transaction.getBook().getSku());
        }

        if (transaction.getFromLocation() != null) {
            dto.setFromLocationId(transaction.getFromLocation().getId());
        }

        if (transaction.getToLocation() != null) {
            dto.setToLocationId(transaction.getToLocation().getId());
        }

        if (transaction.getTransactionType() != null) {
            dto.setTransactionType(transaction.getTransactionType().name());
        }

        dto.setQuantity(transaction.getQuantity());
        dto.setReason(transaction.getReason());

        if (transaction.getUser() != null) {
            dto.setUserId(transaction.getUser().getId());
        }

        dto.setCreatedAt(transaction.getCreatedAt());

        return dto;
    }

    public InventoryTransaction toEntity(InventoryTransactionDTO dto) {
        if (dto == null) return null;

        InventoryTransaction entity = new InventoryTransaction();
        entity.setId(dto.getId());

        if (dto.getTransactionDate() == null) {
            entity.setTransactionDate(LocalDateTime.now());
        } else {
            entity.setTransactionDate(dto.getTransactionDate());
        }

        if (dto.getBookSku() != null && !dto.getBookSku().isEmpty()) {
            Book book = new Book();
            book.setSku(dto.getBookSku());
            entity.setBook(book);
        }

        if (dto.getFromLocationId() != null) {
            BookStockLocation from = new BookStockLocation();
            from.setId(dto.getFromLocationId());
            entity.setFromLocation(from);
        }

        if (dto.getToLocationId() != null) {
            BookStockLocation to = new BookStockLocation();
            to.setId(dto.getToLocationId());
            entity.setToLocation(to);
        }

        if (dto.getTransactionType() != null && !dto.getTransactionType().isEmpty()) {
            try {
                entity.setTransactionType(TransactionType.valueOf(dto.getTransactionType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Tipo de transacción inválido: " + dto.getTransactionType());
            }
        }

        entity.setQuantity(dto.getQuantity());
        entity.setReason(dto.getReason());

        if (dto.getUserId() != null) {
            User user = new User();
            user.setId(dto.getUserId());
            entity.setUser(user);
        }

        // ⚠ createdAt solo si realmente lo necesitas asignar desde DTO
        if (dto.getCreatedAt() != null) {
            entity.setCreatedAt(dto.getCreatedAt());
        }

        return entity;
    }
}
