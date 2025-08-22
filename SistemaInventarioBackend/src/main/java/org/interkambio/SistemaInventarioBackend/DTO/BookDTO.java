package org.interkambio.SistemaInventarioBackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    private String description;
    private List<String> categories;
    private String subjects;
    private List<String> formats;
    private String language;
    private String imageUrl;
    private String websiteUrl;
    private BigDecimal coverPrice;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private BigDecimal fairPrice;
    private String tag;
    private String filter;
    private String productSaleType;

    private Integer totalStock; // suma de stock en todas las ubicaciones
    private List<BookStockLocationDTO> locations;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private SimpleIdNameDTO createdBy;
    private SimpleIdNameDTO updatedBy;
}

