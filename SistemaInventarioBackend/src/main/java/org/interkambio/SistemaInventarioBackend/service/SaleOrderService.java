package org.interkambio.SistemaInventarioBackend.service;

import org.interkambio.SistemaInventarioBackend.DTO.sales.SaleOrderDTO;
import org.interkambio.SistemaInventarioBackend.criteria.SaleOrderSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SaleOrderService extends GenericService<SaleOrderDTO, Long> {
    Page<SaleOrderDTO> findAllOrders(Pageable pageable);
    Optional<SaleOrderDTO> findByOrderNumber(String orderNumber);
    Page<SaleOrderDTO> searchOrders(SaleOrderSearchCriteria criteria, Pageable pageable);
}

