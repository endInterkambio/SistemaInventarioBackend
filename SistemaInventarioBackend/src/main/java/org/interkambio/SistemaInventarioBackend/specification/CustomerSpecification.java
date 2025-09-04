package org.interkambio.SistemaInventarioBackend.specification;

import org.interkambio.SistemaInventarioBackend.criteria.CustomerSearchCriteria;
import org.interkambio.SistemaInventarioBackend.model.Customer;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {

    public static Specification<Customer> withFilters(CustomerSearchCriteria criteria) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Buscar por nombre (PERSON) o companyName (COMPANY)
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

            /* TODO
            // Buscar por email
            if (criteria.getEmail() != null && !criteria.getEmail().isBlank()) {
                predicates.add(builder.like(builder.lower(root.get("email")), "%" + criteria.getEmail().toLowerCase() + "%"));
            }

            // Buscar por teléfono
            if (criteria.getPhoneNumber() != null && !criteria.getPhoneNumber().isBlank()) {
                predicates.add(builder.like(builder.lower(root.get("phoneNumber")), "%" + criteria.getPhoneNumber().toLowerCase() + "%"));
            }*/

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
