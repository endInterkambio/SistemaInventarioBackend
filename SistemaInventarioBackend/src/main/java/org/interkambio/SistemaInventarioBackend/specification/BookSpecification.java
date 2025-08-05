package org.interkambio.SistemaInventarioBackend.specification;

import org.interkambio.SistemaInventarioBackend.criteria.BookSearchCriteria;
import org.interkambio.SistemaInventarioBackend.model.Book;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class BookSpecification {

    public static Specification<Book> withFilters(BookSearchCriteria criteria) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 🔍 Búsqueda general (por múltiples campos)
            if (criteria.getSearch() != null && !criteria.getSearch().isEmpty()) {
                String search = criteria.getSearch().toLowerCase().trim();

                List<Predicate> searchPredicates = new ArrayList<>();
                searchPredicates.add(builder.like(builder.lower(root.get("title")), "%" + search + "%"));
                searchPredicates.add(builder.like(builder.lower(root.get("author")), "%" + search + "%"));
                searchPredicates.add(builder.like(builder.lower(root.get("publisher")), "%" + search + "%"));
                searchPredicates.add(builder.like(builder.lower(root.get("isbn")), "%" + search + "%"));
                searchPredicates.add(builder.like(builder.lower(root.get("sku")), "%" + search + "%"));
                searchPredicates.add(builder.like(builder.lower(root.get("filter")), "%" + search + "%"));
                searchPredicates.add(builder.like(builder.lower(root.get("tag")), "%" + search + "%"));
                searchPredicates.add(builder.like(builder.lower(root.get("subjects")), "%" + search + "%"));

                // Si el texto es numérico, también busca por ISBN numérico exacto
                if (search.matches("\\d+")) {
                    searchPredicates.add(builder.equal(root.get("isbn"), search));
                }

                predicates.add(builder.or(searchPredicates.toArray(new Predicate[0])));
            }
            // 🎯 Filtros individuales
            if (criteria.getTitle() != null)
                predicates.add(builder.like(builder.lower(root.get("title")), "%" + criteria.getTitle().toLowerCase() + "%"));
            if (criteria.getAuthor() != null)
                predicates.add(builder.like(builder.lower(root.get("author")), "%" + criteria.getAuthor().toLowerCase() + "%"));
            if (criteria.getIsbn() != null)
                predicates.add(builder.like(root.get("isbn").as(String.class), "%" + criteria.getIsbn() + "%"));
            if (criteria.getSku() != null)
                predicates.add(builder.like(builder.lower(root.get("sku")), "%" + criteria.getSku().toLowerCase() + "%"));
            if (criteria.getPublisher() != null)
                predicates.add(builder.like(builder.lower(root.get("publisher")), "%" + criteria.getPublisher().toLowerCase() + "%"));
            if (criteria.getCategory() != null)
                predicates.add(builder.like(builder.lower(root.get("category")), "%" + criteria.getCategory().toLowerCase() + "%"));
            if (criteria.getSubjects() != null)
                predicates.add(builder.like(builder.lower(root.get("subjects")), "%" + criteria.getSubjects().toLowerCase() + "%"));
            if (criteria.getFormat() != null)
                predicates.add(builder.like(builder.lower(root.get("format")), "%" + criteria.getFormat().toLowerCase() + "%"));
            if (criteria.getTag() != null)
                predicates.add(builder.like(builder.lower(root.get("tag")), "%" + criteria.getTag().toLowerCase() + "%"));
            if (criteria.getFilter() != null)
                predicates.add(builder.like(builder.lower(root.get("filter")), "%" + criteria.getFilter().toLowerCase() + "%"));

            // 💲 Precio
            if (criteria.getMinPrice() != null)
                predicates.add(builder.greaterThanOrEqualTo(root.get("sellingPrice"), criteria.getMinPrice()));
            if (criteria.getMaxPrice() != null)
                predicates.add(builder.lessThanOrEqualTo(root.get("sellingPrice"), criteria.getMaxPrice()));

            // 📦 Stock
            if (criteria.getMinStock() != null)
                predicates.add(builder.greaterThanOrEqualTo(root.get("stockOnHand"), criteria.getMinStock()));
            if (criteria.getMaxStock() != null)
                predicates.add(builder.lessThanOrEqualTo(root.get("stockOnHand"), criteria.getMaxStock()));

            // 📚 Estante
            if (criteria.getShelf() != null)
                predicates.add(builder.equal(root.get("bookcase"), criteria.getShelf()));

            // 🧱 Piso
            if (criteria.getFloor() != null)
                predicates.add(builder.equal(root.get("bookcaseFloor"), criteria.getFloor()));

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
