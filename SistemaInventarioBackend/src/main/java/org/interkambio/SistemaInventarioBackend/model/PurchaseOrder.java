package org.interkambio.SistemaInventarioBackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "purchase_orders")
@AllArgsConstructor
@NoArgsConstructor

public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "purchase_order_number", unique = true, nullable = false, length = 20)
    private String purchaseOrderNumber; // Ejemplo: PO-00001

    @Column(name= "purchase_date")
    private LocalDateTime purchaseDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id", nullable = false)
    private Supplier supplier;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "purchase_channel", nullable = false, length = 50)
    private String purchaseChannel;

    @Column(name = "amount")
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

    // Relación con items (mappedBy = "order")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderItem> items;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentMade> payments;

    @Column(name = "supplier_notes")
    private String supplierNotes;

    @Column(name = "deliveryDate")
    private LocalDateTime deliveryDate;

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
