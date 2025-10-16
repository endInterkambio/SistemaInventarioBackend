package org.interkambio.SistemaInventarioBackend.service;

import org.interkambio.SistemaInventarioBackend.DTO.purchase.PaymentMadeDTO;
import org.interkambio.SistemaInventarioBackend.model.PaymentMade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface PaymentMadeService extends GenericService<PaymentMadeDTO, Long> {
    List<PaymentMadeDTO> findByPurchaseOrderId(Long id);

    Page<PaymentMadeDTO> findAll(Specification<PaymentMade> spec, Pageable pageable);
}
