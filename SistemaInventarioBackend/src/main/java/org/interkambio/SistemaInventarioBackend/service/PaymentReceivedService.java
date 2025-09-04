package org.interkambio.SistemaInventarioBackend.service;

import org.interkambio.SistemaInventarioBackend.DTO.sales.PaymentReceivedDTO;
import org.interkambio.SistemaInventarioBackend.model.PaymentReceived;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface PaymentReceivedService extends GenericService<PaymentReceivedDTO, Long> {
    List<PaymentReceivedDTO> findBySaleOrderId(Long orderId);

    Page<PaymentReceivedDTO> findAll(Specification<PaymentReceived> spec, Pageable pageable);

}
