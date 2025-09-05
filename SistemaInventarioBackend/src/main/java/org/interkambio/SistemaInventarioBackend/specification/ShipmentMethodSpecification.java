package org.interkambio.SistemaInventarioBackend.specification;

import org.interkambio.SistemaInventarioBackend.criteria.ShipmentMethodSearchCriteria;
import org.interkambio.SistemaInventarioBackend.model.ShipmentMethod;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ShipmentMethodSpecification {

    public static Specification<ShipmentMethod> withFilters(ShipmentMethodSearchCriteria criteria) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getName() != null && !criteria.getName().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("name")), "%" + criteria.getName().toLowerCase() + "%"));
            }

            if (criteria.getDescription() != null && !criteria.getDescription().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("description")), "%" + criteria.getDescription().toLowerCase() + "%"));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
