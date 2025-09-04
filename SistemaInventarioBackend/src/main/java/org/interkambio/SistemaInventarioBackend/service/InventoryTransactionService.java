package org.interkambio.SistemaInventarioBackend.service;

import org.interkambio.SistemaInventarioBackend.DTO.inventory.InventoryTransactionDTO;
import org.interkambio.SistemaInventarioBackend.criteria.InventoryTransactionSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryTransactionService {
    Page<InventoryTransactionDTO> searchTransactions(InventoryTransactionSearchCriteria criteria, Pageable pageable);
    InventoryTransactionDTO createTransaction(InventoryTransactionDTO dto);
}
