package org.interkambio.SistemaInventarioBackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="shipments")

public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "shipment_date")
    private LocalDateTime shipmentDate;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "shipment_fee")
    private BigDecimal shippingFee;
}
