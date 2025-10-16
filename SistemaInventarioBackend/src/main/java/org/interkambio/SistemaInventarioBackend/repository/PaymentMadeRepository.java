package org.interkambio.SistemaInventarioBackend.repository;

import org.interkambio.SistemaInventarioBackend.model.PaymentMade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PaymentMadeRepository extends JpaRepository<PaymentMade, Long>, JpaSpecificationExecutor<PaymentMade> {
    List<PaymentMade> findByPurchaseOrderId(Long id);
}
