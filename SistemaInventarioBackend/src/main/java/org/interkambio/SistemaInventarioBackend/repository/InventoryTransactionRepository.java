package org.interkambio.SistemaInventarioBackend.repository;

import org.interkambio.SistemaInventarioBackend.model.InventoryTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InventoryTransactionRepository extends
        JpaRepository<InventoryTransaction, Long>,
        JpaSpecificationExecutor<InventoryTransaction> {

    @EntityGraph(attributePaths = {
            "book",
            "fromLocation",
            "toLocation",
            "user"
    })
    Page<InventoryTransaction> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {
            "book",
            "fromLocation",
            "toLocation",
            "user"
    })
    Page<InventoryTransaction> findAll(Specification<InventoryTransaction> spec, Pageable pageable);
}
