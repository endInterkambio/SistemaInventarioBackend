package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.InventoryTransactionDTO;
import org.interkambio.SistemaInventarioBackend.model.*;
import org.springframework.stereotype.Component;

@Component
public class InventoryTransactionMapper {

    public InventoryTransactionMapper() {
    }

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

        // Id (útil en updates)
        entity.setId(dto.getId());

        // Fecha de transacción (si viene, la dejamos; si no, BD pondrá valor por defecto)
        if (dto.getTransactionDate() != null) {
            entity.setTransactionDate(dto.getTransactionDate());
        }

        // Libro por SKU (solo referencia ligera)
        if (dto.getBookSku() != null) {
            Book book = new Book();
            book.setSku(dto.getBookSku());
            entity.setBook(book);
        }

        // From location (referencia ligera por id)
        if (dto.getFromLocationId() != null) {
            BookStockLocation from = new BookStockLocation();
            from.setId(dto.getFromLocationId());
            entity.setFromLocation(from);
        }

        // To location (referencia ligera por id)
        if (dto.getToLocationId() != null) {
            BookStockLocation to = new BookStockLocation();
            to.setId(dto.getToLocationId());
            entity.setToLocation(to);
        }

        // Tipo de transacción (validamos que exista en el enum)
        if (dto.getTransactionType() != null) {
            try {
                entity.setTransactionType(TransactionType.valueOf(dto.getTransactionType()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Tipo de transacción inválido: " + dto.getTransactionType());
            }
        }

        // Cantidad y razón
        entity.setQuantity(dto.getQuantity());
        entity.setReason(dto.getReason());

        // Usuario (referencia ligera)
        if (dto.getUserId() != null) {
            User user = new User();
            user.setId(dto.getUserId());
            entity.setUser(user);
        }

        // createdAt (si lo viene el DTO lo respetamos; normalmente lo maneja la BD)
        if (dto.getCreatedAt() != null) {
            entity.setCreatedAt(dto.getCreatedAt());
        }

        return entity;
    }
}
