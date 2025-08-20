package org.interkambio.SistemaInventarioBackend.repository;

import org.interkambio.SistemaInventarioBackend.model.BookStockAdjustment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BookStockAdjustmentRepository extends
        JpaRepository<BookStockAdjustment, Long>,
        JpaSpecificationExecutor<BookStockAdjustment> {

    @EntityGraph(attributePaths = {
            "book",
            "location",
            "performedBy"
    })
    Page<BookStockAdjustment> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {
            "book",
            "location",
            "performedBy"
    })
    Page<BookStockAdjustment> findAll(Specification<BookStockAdjustment> spec, Pageable pageable);

    Optional<BookStockAdjustment> findTopByLocationIdOrderByPerformedAtDesc(Long locationId);
}
