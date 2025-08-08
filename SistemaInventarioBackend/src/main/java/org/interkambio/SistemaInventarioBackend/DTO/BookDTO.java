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

    // 1. Identificación básica
    private Long id;
    private String sku;
    private String title;
    private String isbn;

    // 2. Información de autoría
    private String author;
    private String publisher;

    // 3. Detalles descriptivos
    private String description;
    private String category;
    private String subjects;
    private String format;
    private String language;

    // 4. Recursos multimedia
    private String imageUrl;
    private String websiteUrl;

    // 5. Precios
    private BigDecimal coverPrice;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private BigDecimal fairPrice;

    // 6. Clasificación y filtros
    private String tag;
    private String filter;
    private String productSaleType;

    // 8. Auditoría
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private SimpleIdNameDTO createdBy;
    private SimpleIdNameDTO updatedBy;
}
