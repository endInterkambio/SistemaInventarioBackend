package org.interkambio.SistemaInventarioBackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sale_orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", unique = true, nullable = false, length = 20)
    private String orderNumber; // Ejemplo: SO-00001

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "sale_channel", nullable = false, length = 50)
    private String saleChannel;

    private BigDecimal amount;

    @Column(name = "amount_shipment")
    private BigDecimal amountShipment;

    @Column(name = "additional_fee")
    private BigDecimal additionalFee;

    // Relación con Customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Relación con items (mappedBy = "order")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleOrderItem> items;
}
