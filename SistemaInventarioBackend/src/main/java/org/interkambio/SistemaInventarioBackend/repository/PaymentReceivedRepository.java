package org.interkambio.SistemaInventarioBackend.repository;

import org.interkambio.SistemaInventarioBackend.model.PaymentReceived;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PaymentReceivedRepository
        extends JpaRepository<PaymentReceived, Long>, JpaSpecificationExecutor<PaymentReceived> {

    List<PaymentReceived> findBySaleOrderId(Long orderId);
}

