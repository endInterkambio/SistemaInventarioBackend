package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.BookDTO;
import org.interkambio.SistemaInventarioBackend.model.Book;
import org.interkambio.SistemaInventarioBackend.model.Warehouse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BookMapper implements GenericMapper<Book, BookDTO> {

    @Override
    public Book toEntity(BookDTO dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setSku(dto.getSku());
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setAuthor(dto.getAuthor());
        book.setPublisher(dto.getPublisher());
        book.setStock(dto.getStock());
        book.setBookCondition(dto.getBookCondition());
        book.setDescription(dto.getDescription());
        book.setCategory(dto.getCategory());
        book.setSubjects(dto.getSubjects());
        book.setFormat(dto.getFormat());
        book.setLanguage(dto.getLanguage());
        book.setImageUrl(dto.getImageUrl());
        book.setWebsiteUrl(dto.getWebsiteUrl());
        book.setCoverPrice(dto.getCoverPrice());
        book.setPurchasePrice(dto.getPurchasePrice());
        book.setSellingPrice(dto.getSellingPrice());
        book.setFairPrice(dto.getFairPrice());
        book.setTag(dto.getTag());
        book.setFilter(dto.getFilter());
        book.setProductSaleType(dto.getProductSaleType());
        book.setBookcase(dto.getBookcase());
        book.setBookcaseFloor(dto.getBookcaseFloor());

        if (dto.getWarehouseId() != null) {
            Warehouse warehouse = new Warehouse();
            warehouse.setId(dto.getWarehouseId());
            book.setWarehouse(warehouse);
        } else {
            book.setWarehouse(null);
        }

        book.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now());
        book.setUpdatedAt(dto.getUpdatedAt() != null ? dto.getUpdatedAt() : LocalDateTime.now());
        book.setCreatedBy(dto.getCreatedBy());
        book.setUpdatedBy(dto.getUpdatedBy());

        return book;
    }

    @Override
    public BookDTO toDTO(Book entity) {
        BookDTO dto = new BookDTO();
        dto.setId(entity.getId());
        dto.setSku(entity.getSku());
        dto.setTitle(entity.getTitle());
        dto.setIsbn(entity.getIsbn());
        dto.setAuthor(entity.getAuthor());
        dto.setPublisher(entity.getPublisher());
        dto.setStock(entity.getStock());
        dto.setBookCondition(entity.getBookCondition());
        dto.setDescription(entity.getDescription());
        dto.setCategory(entity.getCategory());
        dto.setSubjects(entity.getSubjects());
        dto.setFormat(entity.getFormat());
        dto.setLanguage(entity.getLanguage());
        dto.setImageUrl(entity.getImageUrl());
        dto.setWebsiteUrl(entity.getWebsiteUrl());
        dto.setCoverPrice(entity.getCoverPrice());
        dto.setPurchasePrice(entity.getPurchasePrice());
        dto.setSellingPrice(entity.getSellingPrice());
        dto.setFairPrice(entity.getFairPrice());
        dto.setTag(entity.getTag());
        dto.setFilter(entity.getFilter());
        dto.setProductSaleType(entity.getProductSaleType());
        dto.setBookcase(entity.getBookcase());
        dto.setBookcaseFloor(entity.getBookcaseFloor());
        dto.setWarehouseId(entity.getWarehouse() != null ? entity.getWarehouse().getId() : null);
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        return dto;
    }
}
