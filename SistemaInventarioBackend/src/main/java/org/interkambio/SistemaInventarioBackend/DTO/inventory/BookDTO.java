package org.interkambio.SistemaInventarioBackend.DTO.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.common.SimpleIdNameDTO;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
    private BigDecimal offerPrice;
    private String tag;
    private String filter;
    private String productSaleType;

    private Integer totalStock; // suma de stock en todas las ubicaciones
    private List<BookStockLocationDTO> locations;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private SimpleIdNameDTO createdBy;
    private SimpleIdNameDTO updatedBy;
}

