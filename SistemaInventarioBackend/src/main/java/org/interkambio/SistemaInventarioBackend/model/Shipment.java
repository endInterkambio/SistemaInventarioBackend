package org.interkambio.SistemaInventarioBackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private SaleOrder order;

    @Column(name = "shipment_date")
    private LocalDateTime shipmentDate;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "shipment_fee")
    private BigDecimal shippingFee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_method_id")
    private ShipmentMethod shipmentMethod;
}
