package org.interkambio.SistemaInventarioBackend.repository;

import org.interkambio.SistemaInventarioBackend.model.SaleOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SaleOrderRepository extends JpaRepository<SaleOrder, Long>, JpaSpecificationExecutor<SaleOrder> {

    Optional<SaleOrder> findByOrderNumber(String orderNumber);

    boolean existsByOrderNumber(String orderNumber);

    @Query("SELECT s.id FROM SaleOrder s")
    Page<Long> findAllIds(Pageable pageable);

    @Query("""
           SELECT DISTINCT s FROM SaleOrder s
           LEFT JOIN FETCH s.createdBy
           LEFT JOIN FETCH s.customer
           LEFT JOIN FETCH s.items i
           LEFT JOIN FETCH i.bookStockLocation bsl
           LEFT JOIN FETCH bsl.book
           WHERE s.id IN :ids
           """)
    List<SaleOrder> findAllWithRelations(@Param("ids") List<Long> ids);
}
