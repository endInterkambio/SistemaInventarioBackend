package org.interkambio.SistemaInventarioBackend.repository;

import org.interkambio.SistemaInventarioBackend.model.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
}


