package org.interkambio.SistemaInventarioBackend.specification;

import jakarta.persistence.criteria.Predicate;
import org.interkambio.SistemaInventarioBackend.criteria.PaymentMadeCriteria;
import org.interkambio.SistemaInventarioBackend.model.PaymentMade;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PaymentMadeSpecification {

    public static Specification<PaymentMade> withFilters(PaymentMadeCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 🔍 Búsqueda global
            if (criteria.getSearch() != null && !criteria.getSearch().isBlank()) {
                String search = "%" + criteria.getSearch().toLowerCase().trim() + "%";
                List<Predicate> searchPredicates = new ArrayList<>();

                searchPredicates.add(cb.like(cb.lower(root.get("paymentMethod")), search));
                searchPredicates.add(cb.like(cb.lower(root.get("referenceNumber")), search));

                predicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));
            }

            // 🎯 Filtros individuales
            if (criteria.getPurchaseOrderId() != null) {
                predicates.add(cb.equal(root.get("purchaseOrder").get("id"), criteria.getPurchaseOrderId()));
            }

            if (criteria.getPaymentDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("paymentDate"), criteria.getPaymentDateFrom()));
            }

            if (criteria.getPaymentDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("paymentDate"), criteria.getPaymentDateTo()));
            }

            if (criteria.getMinAmount() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), criteria.getMinAmount()));
            }

            if (criteria.getMaxAmount() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("amount"), criteria.getMaxAmount()));
            }

            if (criteria.getReferenceNumber() != null && !criteria.getReferenceNumber().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("referenceNumber")),
                        "%" + criteria.getReferenceNumber().toLowerCase() + "%"
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
