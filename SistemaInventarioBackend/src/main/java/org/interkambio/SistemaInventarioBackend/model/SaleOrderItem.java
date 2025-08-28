package org.interkambio.SistemaInventarioBackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sale_order_items")
public class SaleOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación inversa con SaleOrder
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private SaleOrder order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_stock_location_id", nullable = false)
    private BookStockLocation bookStockLocation;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "custom_price")
    private BigDecimal customPrice;
}
