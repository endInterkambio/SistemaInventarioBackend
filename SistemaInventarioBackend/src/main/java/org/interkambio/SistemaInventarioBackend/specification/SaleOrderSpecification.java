package org.interkambio.SistemaInventarioBackend.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.interkambio.SistemaInventarioBackend.criteria.SaleOrderSearchCriteria;
import org.interkambio.SistemaInventarioBackend.model.Customer;
import org.interkambio.SistemaInventarioBackend.model.SaleOrder;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class SaleOrderSpecification {

    public static Specification<SaleOrder> withFilters(SaleOrderSearchCriteria criteria) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 🔍 Búsqueda global
            if (criteria.getSearch() != null && !criteria.getSearch().isEmpty()) {
                String search = "%" + criteria.getSearch().toLowerCase().trim() + "%";

                List<Predicate> searchPredicates = new ArrayList<>();
                searchPredicates.add(builder.like(builder.lower(root.get("orderNumber")), search));
                searchPredicates.add(builder.like(builder.lower(root.get("saleChannel")), search));

                // Join con customer
                Join<SaleOrder, Customer> customerJoin = root.join("customer");
                searchPredicates.add(builder.like(builder.lower(customerJoin.get("name")), search));

                predicates.add(builder.or(searchPredicates.toArray(new Predicate[0])));
            }

            // 🎯 Filtros individuales
            if (criteria.getOrderNumber() != null && !criteria.getOrderNumber().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("orderNumber")), "%" + criteria.getOrderNumber().toLowerCase() + "%"));
            }

            if (criteria.getSaleChannel() != null && !criteria.getSaleChannel().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("saleChannel")), "%" + criteria.getSaleChannel().toLowerCase() + "%"));
            }

            if (criteria.getCustomerName() != null && !criteria.getCustomerName().isEmpty()) {
                Join<SaleOrder, Customer> customerJoin = root.join("customer");
                predicates.add(builder.like(builder.lower(customerJoin.get("name")), "%" + criteria.getCustomerName().toLowerCase() + "%"));
            }

            if (criteria.getStartDate() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("orderDate"), criteria.getStartDate()));
            }

            if (criteria.getEndDate() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("orderDate"), criteria.getEndDate()));
            }

            if (criteria.getStatus() != null) {
                predicates.add(builder.equal(root.get("status"), criteria.getStatus()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
