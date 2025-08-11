package org.interkambio.SistemaInventarioBackend.specification;

import org.interkambio.SistemaInventarioBackend.criteria.BookStockLocationSearchCriteria;
import org.interkambio.SistemaInventarioBackend.model.BookCondition;
import org.interkambio.SistemaInventarioBackend.model.BookStockLocation;
import org.interkambio.SistemaInventarioBackend.specification.base.BaseSpecification;

import java.util.List;

public class BookStockLocationSpecification
        extends BaseSpecification<BookStockLocation, BookStockLocationSearchCriteria> {

    @Override
    protected void addPredicates(BookStockLocationSearchCriteria criteria,
                                 jakarta.persistence.criteria.Root<BookStockLocation> root,
                                 jakarta.persistence.criteria.CriteriaQuery<?> query,
                                 jakarta.persistence.criteria.CriteriaBuilder cb,
                                 List<jakarta.persistence.criteria.Predicate> predicates) {

        if (criteria.getBookSku() != null && !criteria.getBookSku().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("book").get("sku")),
                    "%" + criteria.getBookSku().toLowerCase() + "%"));
        }

        if (criteria.getWarehouseId() != null) {
            predicates.add(cb.equal(root.get("warehouse").get("id"), criteria.getWarehouseId()));
        }

        if (criteria.getBookcase() != null) {
            predicates.add(cb.equal(root.get("bookcase"), criteria.getBookcase()));
        }

        if (criteria.getBookcaseFloor() != null) {
            predicates.add(cb.equal(root.get("bookcaseFloor"), criteria.getBookcaseFloor()));
        }

        if (criteria.getMinStock() != null) {
            predicates.add(cb.ge(root.get("stock"), criteria.getMinStock()));
        }
        if (criteria.getMaxStock() != null) {
            predicates.add(cb.le(root.get("stock"), criteria.getMaxStock()));
        }

        if (criteria.getBookCondition() != null && !criteria.getBookCondition().isEmpty()) {
            try {
                BookCondition cond = BookCondition.valueOf(criteria.getBookCondition().toUpperCase());
                predicates.add(cb.equal(root.get("bookCondition"), cond));
            } catch (IllegalArgumentException ignored) {}
        }
    }
}
