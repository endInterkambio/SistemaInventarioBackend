package org.interkambio.SistemaInventarioBackend.service;

import org.interkambio.SistemaInventarioBackend.DTO.InventoryTransactionDTO;

import java.util.List;

public interface InventoryTransactionService {
    InventoryTransactionDTO createTransaction(InventoryTransactionDTO dto);
    List<InventoryTransactionDTO> getAllTransactions();
    InventoryTransactionDTO getTransactionById(Long id);
}
