package org.interkambio.SistemaInventarioBackend.repository;

import org.interkambio.SistemaInventarioBackend.model.InventoryTransaction;
import org.interkambio.SistemaInventarioBackend.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface InventoryTransactionRepository extends
        JpaRepository<InventoryTransaction, Long>,
        JpaSpecificationExecutor<InventoryTransaction> {

    boolean existsByFromLocationId(Long fromLocationId);

    boolean existsByToLocationId(Long toLocationId);

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

    // Suma de entradas hacia la ubicación
    @Query("SELECT SUM(tx.quantity) FROM InventoryTransaction tx " +
            "WHERE tx.toLocation.id = :locationId " +
            "AND tx.transactionType IN ('ADJUSTMENT','PURCHASE','RETURN_IN','TRANSFER')")
    Integer sumQuantityByToLocation(@Param("locationId") Long locationId);

    // Suma de salidas desde la ubicación
    @Query("SELECT SUM(tx.quantity) FROM InventoryTransaction tx " +
            "WHERE tx.fromLocation.id = :locationId " +
            "AND tx.transactionType IN ('SALE','RETURN_OUT','TRANSFER')")
    Integer sumQuantityByFromLocation(@Param("locationId") Long locationId);

    Optional<InventoryTransaction> findTopByToLocationIdAndTransactionTypeOrderByTransactionDateDesc(
            Long locationId,
            TransactionType transactionType
    );

    @Query("SELECT SUM(tx.quantity) FROM InventoryTransaction tx " +
            "WHERE tx.toLocation.id = :locationId " +
            "AND tx.transactionDate < :beforeDate " +
            "AND tx.transactionType IN ('ADJUSTMENT','PURCHASE','RETURN_IN','TRANSFER')")
    Integer sumQuantityByToLocationBefore(@Param("locationId") Long locationId,
                                          @Param("beforeDate") OffsetDateTime beforeDate);

    @Query("SELECT SUM(tx.quantity) FROM InventoryTransaction tx " +
            "WHERE tx.fromLocation.id = :locationId " +
            "AND tx.transactionDate < :beforeDate " +
            "AND tx.transactionType IN ('SALE','RETURN_OUT','TRANSFER','ADJUSTMENT')")
    Integer sumQuantityByFromLocationBefore(@Param("locationId") Long locationId,
                                            @Param("beforeDate") OffsetDateTime beforeDate);

    @Query("SELECT tx FROM InventoryTransaction tx " +
            "WHERE (tx.toLocation.id = :locationId OR tx.fromLocation.id = :locationId) " +
            "AND tx.transactionType IN :types " +
            "ORDER BY tx.transactionDate DESC, tx.id DESC")
    Page<InventoryTransaction> findRelevantTransactions(
            @Param("locationId") Long locationId,
            @Param("types") List<TransactionType> types,
            Pageable pageable
    );


}
