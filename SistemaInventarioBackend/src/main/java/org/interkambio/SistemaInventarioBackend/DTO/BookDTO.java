package org.interkambio.SistemaInventarioBackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    private Long id;
    private String sku;
    private String title;
    private String isbn;
    private String author;
    private String publisher;
    private Integer stockOnHand;
    private String book_condition;
    private String description;
    private String category;
    private String subjects;
    private String format;
    private String language;
    private String imageUrl;
    private String websiteUrl;
    private String tag;
    private String productSaleType;
    private Integer bookcase;
    private Integer bookcaseFloor;
    private BigDecimal coverPrice;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private BigDecimal fairPrice;
    private String filter;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
}
