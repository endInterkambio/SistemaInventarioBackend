package org.interkambio.SistemaInventarioBackend.specification.base;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseSpecification<T, C> {

    public Specification<T> build(C criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            addPredicates(criteria, root, query, cb, predicates);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    protected abstract void addPredicates(C criteria,
                                          jakarta.persistence.criteria.Root<T> root,
                                          jakarta.persistence.criteria.CriteriaQuery<?> query,
                                          jakarta.persistence.criteria.CriteriaBuilder cb,
                                          List<Predicate> predicates);
}
