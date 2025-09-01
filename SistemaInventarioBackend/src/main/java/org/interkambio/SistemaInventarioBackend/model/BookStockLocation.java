package org.interkambio.SistemaInventarioBackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(
        name = "book_stock_locations",
        uniqueConstraints = @UniqueConstraint(columnNames = {
                "book_id", "warehouse_id", "bookcase", "bookcase_floor", "book_condition", "location_type"
        })
)
@NoArgsConstructor
public class BookStockLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Book usando book_id como FK
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // Mantener bookSku por compatibilidad (no FK, simple columna)
    @Column(name = "book_SKU", length = 36)
    private String bookSku;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "bookcase")
    private Integer bookcase;

    @Column(name = "bookcase_floor")
    private Integer bookcaseFloor;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_condition", nullable = false, length = 1)
    private BookCondition bookCondition = BookCondition.U;

    @Enumerated(EnumType.STRING)
    @Column(name = "location_type", nullable = false, length = 50)
    private LocationType locationType = LocationType.MAIN_STORAGE;

    @Column(name = "last_updated_at", insertable = false, updatable = false)
    private LocalDateTime lastUpdatedAt;

    @Transient
    public String getDisplayName() {
        return warehouse.getName() + " - Estante " + bookcase + " Piso " + bookcaseFloor;
    }
}
