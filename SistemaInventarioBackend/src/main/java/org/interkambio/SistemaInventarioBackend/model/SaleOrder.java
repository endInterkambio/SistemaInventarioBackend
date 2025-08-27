package org.interkambio.SistemaInventarioBackend.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sale_orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK técnico

    @Column(name = "order_number", unique = true, nullable = false, length = 20)
    private String orderNumber; // SO-00001

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "sale_channel", nullable = false, length = 50)
    private String saleChannel;

    private BigDecimal amount;

    @Column(name = "amount_shipment")
    private BigDecimal amountShipment;

    @Column(name = "additional_fee")
    private BigDecimal additionalFee;
}
