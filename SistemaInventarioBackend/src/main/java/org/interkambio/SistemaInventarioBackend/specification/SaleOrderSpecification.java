package org.interkambio.SistemaInventarioBackend.specification;

import jakarta.persistence.criteria.Join;
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

            // 🔍 Filtrar por número de orden
            if (criteria.getOrderNumber() != null && !criteria.getOrderNumber().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("orderNumber")), "%" + criteria.getOrderNumber().toLowerCase() + "%"));
            }

            // 🔍 Filtrar por canal de venta
            if (criteria.getSaleChannel() != null && !criteria.getSaleChannel().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("saleChannel")), "%" + criteria.getSaleChannel().toLowerCase() + "%"));
            }

            // 🔍 Filtrar por rango de fechas
            if (criteria.getStartDate() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("orderDate"), criteria.getStartDate()));
            }
            if (criteria.getEndDate() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("orderDate"), criteria.getEndDate()));
            }

            // 🔍 Filtrar por nombre de cliente (join)
            if (criteria.getCustomerName() != null && !criteria.getCustomerName().isEmpty()) {
                Join<SaleOrder, Customer> customerJoin = root.join("customer");
                predicates.add(builder.like(builder.lower(customerJoin.get("name")), "%" + criteria.getCustomerName().toLowerCase() + "%"));
            }

            // 🔍 Filtrar por status de orden
            if (criteria.getStatus() != null) {
                predicates.add(builder.equal(root.get("status"), criteria.getStatus()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

