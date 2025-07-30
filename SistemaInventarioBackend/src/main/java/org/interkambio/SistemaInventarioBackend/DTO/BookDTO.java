package org.interkambio.SistemaInventarioBackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.interkambio.SistemaInventarioBackend.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BookDTO {

    private Long id;
    private String SKU;
    private String title;
    private String ISBN;
    private String author;
    private String publisher;
    private String description;
    private String category;
    private String subjects;
    private String format;
    private String language;
    private String imageUrl;
    private String websiteUrl;
    private String tag;
    private Integer bookcase;
    private Integer bookcaseFloor;
    private BigDecimal coverPrice;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private String filter;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private User createdBy;
    private User updatedBy;
}
