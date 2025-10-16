package org.interkambio.SistemaInventarioBackend.repository;

import org.interkambio.SistemaInventarioBackend.model.PurchaseOrder;
import org.interkambio.SistemaInventarioBackend.model.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long>, JpaSpecificationExecutor<PurchaseOrder> {

    Optional<PurchaseOrder> findByPurchaseOrderNumber(String purchaseOrderNumber);

    boolean existsByPurchaseOrderNumber(String purchaseOrderNumber);

    @Query("SELECT p.purchaseOrderNumber FROM PurchaseOrder p ORDER BY p.id DESC")
    List<String> findLastPurchaseOrderNumber(Pageable pageable);

    @Query("SELECT p.id FROM PurchaseOrder p")
    Page<Long> findAllIds(Pageable pageable);

    @Query("""
            SELECT DISTINCT p FROM PurchaseOrder p
            LEFT JOIN FETCH p.createdBy
            LEFT JOIN FETCH p.items i
            LEFT JOIN FETCH i.bookStockLocation bsl
            LEFT JOIN FETCH bsl.book
            WHERE p.id IN :ids
            """)
    List<PurchaseOrder> findAllWithRelations(@Param("ids") List<Long> ids);

    @Query("SELECT p.purchaseOrderNumber FROM PurchaseOrder p ORDER BY p.id DESC")
    List<String> findLastOrderNumber(Pageable pageable);
}
