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

    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_sku", referencedColumnName = "sku", nullable = false)
    private Book book;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "custom_price")
    private BigDecimal customPrice;
}
