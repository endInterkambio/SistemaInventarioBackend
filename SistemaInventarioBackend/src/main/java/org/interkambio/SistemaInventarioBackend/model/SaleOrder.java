package org.interkambio.SistemaInventarioBackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
    private OffsetDateTime orderDate;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    // Relación con Customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Relación con items (mappedBy = "order")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleOrderItem> items;

    @OneToMany(mappedBy = "saleOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentReceived> payments;

    @Column(name= "customer_notes")
    private String customerNotes;

    // relación 1:1 con Shipment (opcional)
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Shipment shipment;

    // ✅ Método de dominio para calcular el total de la orden
    public BigDecimal getTotalAmount() {
        return (amount != null ? amount : BigDecimal.ZERO)
                .add(amountShipment != null ? amountShipment : BigDecimal.ZERO)
                .add(additionalFee != null ? additionalFee : BigDecimal.ZERO);
    }

    // ✅ Método de dominio para calcular el total pagado
    public BigDecimal getTotalPaid() {
        if (payments == null) return BigDecimal.ZERO;
        return payments.stream()
                .map(p -> p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
