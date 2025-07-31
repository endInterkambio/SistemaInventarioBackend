package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.BookDTO;
import org.interkambio.SistemaInventarioBackend.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper implements GenericMapper<Book, BookDTO> {

    @Override
    public Book toEntity(BookDTO dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setSku(dto.getSku());
        book.setIsbn(dto.getIsbn());
        book.setAuthor(dto.getAuthor());
        book.setPublisher(dto.getPublisher());
        book.setCondition(dto.getCondition());
        book.setDescription(dto.getDescription());
        book.setCategory(dto.getCategory());
        book.setSubjects(dto.getSubjects());
        book.setFormat(dto.getFormat());
        book.setLanguage(dto.getLanguage());
        book.setImageUrl(dto.getImageUrl());
        book.setWebsiteUrl(dto.getWebsiteUrl());
        book.setTag(dto.getTag());
        book.setProductSaleType(dto.getProductSaleType());
        book.setBookcase(dto.getBookcase());
        book.setBookcaseFloor(dto.getBookcaseFloor());
        book.setCoverPrice(dto.getCoverPrice());
        book.setPurchasePrice(dto.getPurchasePrice());
        book.setSellingPrice(dto.getSellingPrice());
        book.setFairPrice(dto.getFairPrice());
        book.setFilter(dto.getFilter());

        book.setCreatedBy(dto.getCreatedBy());
        book.setUpdatedBy(dto.getUpdatedBy());

        return book;
    }

    @Override
    public BookDTO toDTO(Book entity) {
        return new BookDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getSku(),
                entity.getIsbn(),
                entity.getAuthor(),
                entity.getPublisher(),
                entity.getCondition(),
                entity.getDescription(),
                entity.getCategory(),
                entity.getSubjects(),
                entity.getFormat(),
                entity.getLanguage(),
                entity.getImageUrl(),
                entity.getWebsiteUrl(),
                entity.getTag(),
                entity.getProductSaleType(),
                entity.getBookcase(),
                entity.getBookcaseFloor(),
                entity.getCoverPrice(),
                entity.getPurchasePrice(),
                entity.getSellingPrice(),
                entity.getFairPrice(),
                entity.getFilter(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getCreatedBy(),
                entity.getUpdatedBy()
        );
    }
}
