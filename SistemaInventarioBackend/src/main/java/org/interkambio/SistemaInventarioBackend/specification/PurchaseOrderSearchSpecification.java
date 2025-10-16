package org.interkambio.SistemaInventarioBackend.specification;

import jakarta.persistence.criteria.Join;
import org.interkambio.SistemaInventarioBackend.criteria.PurchaseOrderSearchCriteria;
import org.interkambio.SistemaInventarioBackend.model.PurchaseOrder;
import org.interkambio.SistemaInventarioBackend.model.Supplier;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderSearchSpecification {

    public static Specification<PurchaseOrder> withFilters(PurchaseOrderSearchCriteria criteria) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getSearch() != null && !criteria.getSearch().isEmpty()) {
                String search = "%" + criteria.getSearch().toLowerCase().trim() + "%";

                List<Predicate> searchPredicates = new ArrayList<>();
                searchPredicates.add(builder.like(builder.lower(root.get("purchaseOrderNumber")), search));

                Join<PurchaseOrder, Supplier> supplierJoin = root.join("supplier");
                searchPredicates.add(builder.like(builder.lower(supplierJoin.get("name")), search));

                predicates.add(builder.or(searchPredicates.toArray(new Predicate[0])));
            }

            if (criteria.getPurchaseOrderNumber() != null && !criteria.getPurchaseOrderNumber().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("purchaseOrderNumber")),
                        "%" + criteria.getPurchaseOrderNumber().toLowerCase() + "%"));
            }

            if (criteria.getSupplierName() != null && !criteria.getSupplierName().isEmpty()) {
                Join<PurchaseOrder, Supplier> supplierJoin = root.join("supplier");
                predicates.add(builder.like(builder.lower(supplierJoin.get("name")),
                        "%" + criteria.getSupplierName().toLowerCase() + "%"));
            }

            if (criteria.getStartDate() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("purchaseDate"), criteria.getStartDate()));
            }

            if (criteria.getEndDate() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("purchaseDate"), criteria.getEndDate()));
            }

            if (criteria.getStatus() != null) {
                predicates.add(builder.equal(root.get("status"), criteria.getStatus()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
