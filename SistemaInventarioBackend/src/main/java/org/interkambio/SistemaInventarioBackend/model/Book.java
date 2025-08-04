package org.interkambio.SistemaInventarioBackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "books")
@NoArgsConstructor
public class Book {

    // ========================
    // 1. Identificación básica
    // ========================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sku", unique = true)
    private String sku;

    @Column(name = "title")
    private String title;

    @Column(name = "isbn")
    private String isbn;

    // ========================
    // 2. Información de autoría
    // ========================
    @Column(name = "author")
    private String author;

    @Column(name = "publisher")
    private String publisher;

    // ========================
    // 3. Detalles descriptivos
    // ========================
    @Column(name = "stock")
    private Integer stock;

    @Column(name = "book_condition")
    private String bookCondition;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "subjects")
    private String subjects;

    @Column(name = "format")
    private String format;

    @Column(name = "language")
    private String language;

    // ========================
    // 4. Recursos multimedia
    // ========================
    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "website_url")
    private String websiteUrl;

    // ========================
    // 5. Precios
    // ========================
    @Column(name = "cover_price")
    private BigDecimal coverPrice;

    @Column(name = "purchase_price")
    private BigDecimal purchasePrice;

    @Column(name = "selling_price")
    private BigDecimal sellingPrice;

    @Column(name = "fair_price")
    private BigDecimal fairPrice;

    // ========================
    // 6. Clasificación y filtros
    // ========================
    @Column(name = "tag")
    private String tag;

    @Column(name = "filter")
    private String filter;

    @Column(name = "product_sale_type")
    private String productSaleType;

    // ========================
    // 7. Ubicación física
    // ========================
    @Column(name = "bookcase")
    private Integer bookcase;

    @Column(name = "bookcase_floor")
    private Integer bookcaseFloor;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    // ========================
    // 8. Auditoría
    // ========================
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;
}
