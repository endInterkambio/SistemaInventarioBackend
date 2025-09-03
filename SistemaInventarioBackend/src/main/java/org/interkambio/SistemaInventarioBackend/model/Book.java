package org.interkambio.SistemaInventarioBackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

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
    // 8. Auditoría
    // ========================
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    // Relación con ubicaciones de stock (sin cascada para no borrar stock accidentalmente)
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<BookStockLocation> stockLocations = new ArrayList<>();
}
