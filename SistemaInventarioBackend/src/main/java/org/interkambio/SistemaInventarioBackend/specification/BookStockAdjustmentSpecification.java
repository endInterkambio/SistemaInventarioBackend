package org.interkambio.SistemaInventarioBackend.specification;

import jakarta.persistence.criteria.Predicate;
import org.interkambio.SistemaInventarioBackend.criteria.BookStockAdjustmentSearchCriteria;
import org.interkambio.SistemaInventarioBackend.model.BookStockAdjustment;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BookStockAdjustmentSpecification {

    public static Specification<BookStockAdjustment> withFilters(BookStockAdjustmentSearchCriteria criteria) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 📦 Filtrar por SKU del libro
            if (criteria.getBookSku() != null && !criteria.getBookSku().isBlank()) {
                predicates.add(
                        builder.like(
                                builder.lower(root.get("bookSku")),
                                "%" + criteria.getBookSku().toLowerCase() + "%"
                        )
                );
            }

            // 📍 Filtrar por ubicación
            if (criteria.getLocationId() != null) {
                predicates.add(
                        builder.equal(root.get("location").get("id"), criteria.getLocationId())
                );
            }

            // 👤 Filtrar por usuario que hizo el ajuste
            if (criteria.getPerformedById() != null) {
                predicates.add(
                        builder.equal(root.get("performedBy").get("id"), criteria.getPerformedById())
                );
            }

            // 📝 Filtrar por motivo del ajuste
            if (criteria.getReason() != null && !criteria.getReason().isBlank()) {
                predicates.add(
                        builder.like(
                                builder.lower(root.get("reason")),
                                "%" + criteria.getReason().toLowerCase() + "%"
                        )
                );
            }

            // 📅 Filtrar por rango de fechas
            if (criteria.getFromDate() != null && criteria.getToDate() != null) {
                predicates.add(
                        builder.between(root.get("performedAt"), criteria.getFromDate(), criteria.getToDate())
                );
            } else if (criteria.getFromDate() != null) {
                predicates.add(
                        builder.greaterThanOrEqualTo(root.get("performedAt"), criteria.getFromDate())
                );
            } else if (criteria.getToDate() != null) {
                predicates.add(
                        builder.lessThanOrEqualTo(root.get("performedAt"), criteria.getToDate())
                );
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
