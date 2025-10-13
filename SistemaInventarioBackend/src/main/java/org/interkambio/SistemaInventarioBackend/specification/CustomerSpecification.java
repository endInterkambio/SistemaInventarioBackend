package org.interkambio.SistemaInventarioBackend.specification;

import jakarta.persistence.criteria.Predicate;
import org.interkambio.SistemaInventarioBackend.criteria.CustomerSearchCriteria;
import org.interkambio.SistemaInventarioBackend.model.Customer;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {

    public static Specification<Customer> withFilters(CustomerSearchCriteria criteria) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 🔍 Búsqueda global
            if (criteria.getSearch() != null && !criteria.getSearch().isBlank()) {
                String search = "%" + criteria.getSearch().toLowerCase().trim() + "%";
                List<Predicate> searchPredicates = new ArrayList<>();

                // Campos para PERSON
                searchPredicates.add(
                        builder.and(
                                builder.equal(root.get("customerType"), "PERSON"),
                                builder.like(builder.lower(root.get("name")), search)
                        )
                );

                // Campos para COMPANY
                searchPredicates.add(
                        builder.and(
                                builder.equal(root.get("customerType"), "COMPANY"),
                                builder.like(builder.lower(root.get("companyName")), search)
                        )
                );

                // Otros campos comunes
                if (root.get("email") != null) {
                    searchPredicates.add(builder.like(builder.lower(root.get("email")), search));
                }
                if (root.get("phoneNumber") != null) {
                    searchPredicates.add(builder.like(builder.lower(root.get("phoneNumber")), search));
                }

                if (root.get("documentNumber") != null) {
                    searchPredicates.add(builder.like(builder.lower(root.get("documentNumber")), search));
                }

                predicates.add(builder.or(searchPredicates.toArray(new Predicate[0])));
            }

            // 🎯 Filtros individuales
            if (criteria.getName() != null && !criteria.getName().isBlank()) {
                String pattern = "%" + criteria.getName().toLowerCase() + "%";

                predicates.add(
                        builder.or(
                                builder.and(
                                        builder.equal(root.get("customerType"), "PERSON"),
                                        builder.like(builder.lower(root.get("name")), pattern)
                                ),
                                builder.and(
                                        builder.equal(root.get("customerType"), "COMPANY"),
                                        builder.like(builder.lower(root.get("companyName")), pattern)
                                )
                        )
                );
            }

            if (criteria.getEmail() != null && !criteria.getEmail().isBlank()) {
                predicates.add(builder.like(builder.lower(root.get("email")), "%" + criteria.getEmail().toLowerCase() + "%"));
            }

            if (criteria.getPhoneNumber() != null && !criteria.getPhoneNumber().isBlank()) {
                predicates.add(builder.like(builder.lower(root.get("phoneNumber")), "%" + criteria.getPhoneNumber().toLowerCase() + "%"));
            }

            if (criteria.getCustomerType() != null && !criteria.getCustomerType().isBlank()) {
                predicates.add(builder.equal(root.get("customerType"), criteria.getCustomerType()));
            }

            if (criteria.getDocumentNumber() != null && !criteria.getDocumentNumber().isBlank()) {
                predicates.add(builder.equal(root.get("documentNumber"), criteria.getDocumentNumber()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
