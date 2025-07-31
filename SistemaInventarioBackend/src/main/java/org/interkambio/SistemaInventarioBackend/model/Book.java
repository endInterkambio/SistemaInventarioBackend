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
@Table(name="books")
@NoArgsConstructor

public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(unique = true)
    private String sku;

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

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @Column(name = "updated_by", nullable = true)
    private Long updatedBy;
}
