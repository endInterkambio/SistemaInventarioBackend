package org.interkambio.SistemaInventarioBackend.specification;

import jakarta.persistence.criteria.Predicate;
import org.interkambio.SistemaInventarioBackend.criteria.SupplierSearchCriteria;
import org.interkambio.SistemaInventarioBackend.model.Supplier;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SupplierSpecification {

    public static Specification<Supplier> withFilters(SupplierSearchCriteria criteria) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 🔍 Búsqueda global
            if (criteria.getSearch() != null && !criteria.getSearch().isBlank()) {
                String search = "%" + criteria.getSearch().toLowerCase().trim() + "%";
                List<Predicate> searchPredicates = new ArrayList<>();

                searchPredicates.add(builder.like(builder.lower(root.get("name")), search));
                searchPredicates.add(builder.like(builder.lower(root.get("contactPerson")), search));
                searchPredicates.add(builder.like(builder.lower(root.get("email")), search));
                searchPredicates.add(builder.like(builder.lower(root.get("phoneNumber")), search));
                searchPredicates.add(builder.like(builder.lower(root.get("address")), search));

                predicates.add(builder.or(searchPredicates.toArray(new Predicate[0])));
            }

            // 🎯 Filtros individuales
            if (criteria.getName() != null && !criteria.getName().isBlank()) {
                predicates.add(builder.like(builder.lower(root.get("name")), "%" + criteria.getName().toLowerCase() + "%"));
            }

            if (criteria.getContactPerson() != null && !criteria.getContactPerson().isBlank()) {
                predicates.add(builder.like(builder.lower(root.get("contactPerson")), "%" + criteria.getContactPerson().toLowerCase() + "%"));
            }

            if (criteria.getEmail() != null && !criteria.getEmail().isBlank()) {
                predicates.add(builder.like(builder.lower(root.get("email")), "%" + criteria.getEmail().toLowerCase() + "%"));
            }

            if (criteria.getPhoneNumber() != null && !criteria.getPhoneNumber().isBlank()) {
                predicates.add(builder.like(builder.lower(root.get("phoneNumber")), "%" + criteria.getPhoneNumber().toLowerCase() + "%"));
            }

            if (criteria.getAddress() != null && !criteria.getAddress().isBlank()) {
                predicates.add(builder.like(builder.lower(root.get("address")), "%" + criteria.getAddress().toLowerCase() + "%"));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
