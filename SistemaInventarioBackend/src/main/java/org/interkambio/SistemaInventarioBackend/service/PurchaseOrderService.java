package org.interkambio.SistemaInventarioBackend.service;

import org.interkambio.SistemaInventarioBackend.DTO.purchase.PurchaseOrderDTO;
import org.interkambio.SistemaInventarioBackend.criteria.PurchaseOrderSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PurchaseOrderService extends GenericService<PurchaseOrderDTO, Long> {
    Page<PurchaseOrderDTO> findAllOrders(Pageable pageable);

    Optional<PurchaseOrderDTO> findByPurchaseOrderNumber(String purchaseOrderNumber);

    Page<PurchaseOrderDTO> searchPurchaseOrders(PurchaseOrderSearchCriteria criteria, Pageable pageable);

    String getNextOrderNumber();
}
