package org.interkambio.SistemaInventarioBackend.specification;

import org.interkambio.SistemaInventarioBackend.criteria.BookStockAdjustmentSearchCriteria;
import org.interkambio.SistemaInventarioBackend.model.BookStockAdjustment;
import org.interkambio.SistemaInventarioBackend.specification.base.BaseSpecification;

import java.util.List;

public class BookStockAdjustmentSpecification
        extends BaseSpecification<BookStockAdjustment, BookStockAdjustmentSearchCriteria> {

    @Override
    protected void addPredicates(BookStockAdjustmentSearchCriteria criteria,
                                 jakarta.persistence.criteria.Root<BookStockAdjustment> root,
                                 jakarta.persistence.criteria.CriteriaQuery<?> query,
                                 jakarta.persistence.criteria.CriteriaBuilder cb,
                                 List<jakarta.persistence.criteria.Predicate> predicates) {

        // 📦 Filtrar por SKU del libro
        if (criteria.getBookSku() != null && !criteria.getBookSku().isBlank()) {
            predicates.add(cb.like(
                    cb.lower(root.get("bookSku")),
                    "%" + criteria.getBookSku().toLowerCase() + "%"
            ));
        }

        // 📍 Filtrar por ubicación
        if (criteria.getLocationId() != null) {
            predicates.add(cb.equal(root.get("location").get("id"), criteria.getLocationId()));
        }

        // 👤 Filtrar por usuario que hizo el ajuste
        if (criteria.getPerformedById() != null) {
            predicates.add(cb.equal(root.get("performedBy").get("id"), criteria.getPerformedById()));
        }

        // 📝 Filtrar por motivo del ajuste
        if (criteria.getReason() != null && !criteria.getReason().isBlank()) {
            predicates.add(cb.like(
                    cb.lower(root.get("reason")),
                    "%" + criteria.getReason().toLowerCase() + "%"
            ));
        }

        // 📅 Filtrar por rango de fechas
        if (criteria.getFromDate() != null && criteria.getToDate() != null) {
            predicates.add(cb.between(root.get("performedAt"), criteria.getFromDate(), criteria.getToDate()));
        } else if (criteria.getFromDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("performedAt"), criteria.getFromDate()));
        } else if (criteria.getToDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("performedAt"), criteria.getToDate()));
        }
    }
}
