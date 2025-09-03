package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.BookDTO;
import org.interkambio.SistemaInventarioBackend.DTO.SimpleIdNameDTO;
import org.interkambio.SistemaInventarioBackend.model.Book;
import org.interkambio.SistemaInventarioBackend.model.BookStockLocation;
import org.interkambio.SistemaInventarioBackend.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class BookMapper implements GenericMapper<Book, BookDTO> {

    private final BookStockLocationMapper stockLocationMapper;

    public BookMapper(BookStockLocationMapper stockLocationMapper) {
        this.stockLocationMapper = stockLocationMapper;
    }

    @Override
    public Book toEntity(BookDTO dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setSku(dto.getSku());
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setAuthor(dto.getAuthor());
        book.setPublisher(dto.getPublisher());
        book.setDescription(dto.getDescription());
        book.setCategory(
                dto.getCategories() != null && !dto.getCategories().isEmpty()
                        ? String.join(",", dto.getCategories())
                        : null
        );
        book.setSubjects(dto.getSubjects());
        book.setFormat(
                dto.getFormats() != null && !dto.getFormats().isEmpty()
                        ? String.join(",", dto.getFormats())
                        : null
        );
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
        book.setCreatedAt(dto.getCreatedAt());
        book.setUpdatedAt(dto.getUpdatedAt());

        if (dto.getCreatedBy() != null && dto.getCreatedBy().getId() != null) {
            User createdBy = new User();
            createdBy.setId(dto.getCreatedBy().getId());
            book.setCreatedBy(createdBy);
        }

        if (dto.getUpdatedBy() != null && dto.getUpdatedBy().getId() != null) {
            User updatedBy = new User();
            updatedBy.setId(dto.getUpdatedBy().getId());
            book.setUpdatedBy(updatedBy);
        }

        // 📌 NUEVO: mapear locations desde DTO
        if (dto.getLocations() != null && !dto.getLocations().isEmpty()) {
            List<BookStockLocation> stockLocations = dto.getLocations().stream()
                    .map(stockLocationMapper::toEntity)
                    .peek(loc -> loc.setBook(book)) // aseguramos la relación bidireccional
                    .toList();

            book.setStockLocations(stockLocations);
        }

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
        dto.setDescription(entity.getDescription());
        dto.setCategories(
                entity.getCategory() != null
                        ? Arrays.asList(entity.getCategory().split("[,;/]")) // <- acepta coma, punto y coma o slash
                        : new ArrayList<>()
        );
        dto.setSubjects(entity.getSubjects());
        dto.setFormats(
                entity.getFormat() != null
                        ? Arrays.asList(entity.getFormat().split("[,;/]")) // <- acepta coma, punto y coma o slash
                        : new ArrayList<>()
        );
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
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getCreatedBy() != null) {
            dto.setCreatedBy(new SimpleIdNameDTO(
                    entity.getCreatedBy().getId(),
                    entity.getCreatedBy().getUsername()
            ));
        }

        if (entity.getUpdatedBy() != null) {
            dto.setUpdatedBy(new SimpleIdNameDTO(
                    entity.getUpdatedBy().getId(),
                    entity.getUpdatedBy().getUsername()
            ));
        }

        // 📌 Reutilizar mapper de locations
        if (entity.getStockLocations() != null) {
            dto.setLocations(
                    entity.getStockLocations().stream()
                            .map(stockLocationMapper::toDTO)
                            .toList()
            );

            // 📌 Calcular totalStock
            dto.setTotalStock(
                    entity.getStockLocations().stream()
                            .mapToInt(BookStockLocation::getStock)
                            .sum()
            );
        } else {
            dto.setLocations(List.of());
            dto.setTotalStock(0);
        }

        return dto;
    }

}

