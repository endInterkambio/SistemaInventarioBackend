package org.interkambio.SistemaInventarioBackend.repository;

import org.interkambio.SistemaInventarioBackend.model.PurchaseOrder;
import org.interkambio.SistemaInventarioBackend.model.SaleOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long>, JpaSpecificationExecutor<PurchaseOrder> {

    Optional<SaleOrder> findByPurchaseOrderNumber(String purchaseOrderNumber);

    boolean existsByPurchaseOrderNumber(String purchaseOrderNumber);

    @Query("SELECT p.purchaseOrderNumber FROM PurchaseOrder p ORDER BY p.id DESC")
    List<String> findLastPurchaseOrderNumber(Pageable pageable);

    @Query("SELECT p.id FROM PurchaseOrder p")
    Page<Long> findAllIds(Pageable pageable);
}
