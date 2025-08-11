package org.interkambio.SistemaInventarioBackend.specification;

import org.interkambio.SistemaInventarioBackend.criteria.InventoryTransactionSearchCriteria;
import org.interkambio.SistemaInventarioBackend.model.InventoryTransaction;
import org.interkambio.SistemaInventarioBackend.specification.base.BaseSpecification;

import java.util.List;

public class InventoryTransactionSpecification
        extends BaseSpecification<InventoryTransaction, InventoryTransactionSearchCriteria> {

    @Override
    protected void addPredicates(InventoryTransactionSearchCriteria criteria,
                                 jakarta.persistence.criteria.Root<InventoryTransaction> root,
                                 jakarta.persistence.criteria.CriteriaQuery<?> query,
                                 jakarta.persistence.criteria.CriteriaBuilder cb,
                                 List<jakarta.persistence.criteria.Predicate> predicates) {

        // 📦 Filtrar por SKU
        if (criteria.getBookSku() != null && !criteria.getBookSku().isEmpty()) {
            predicates.add(cb.like(
                    cb.lower(root.get("bookSku")),
                    "%" + criteria.getBookSku().toLowerCase() + "%"
            ));
        }

        // 📍 Desde ubicación
        if (criteria.getFromLocationId() != null) {
            predicates.add(cb.equal(root.get("fromLocationId"), criteria.getFromLocationId()));
        }

        // 📍 Hacia ubicación
        if (criteria.getToLocationId() != null) {
            predicates.add(cb.equal(root.get("toLocationId"), criteria.getToLocationId()));
        }

        // 🔄 Tipo de transacción
        if (criteria.getTransactionType() != null && !criteria.getTransactionType().isEmpty()) {
            predicates.add(cb.equal(
                    cb.lower(root.get("transactionType")),
                    criteria.getTransactionType().toLowerCase()
            ));
        }

        // 📝 Motivo
        if (criteria.getReason() != null && !criteria.getReason().isEmpty()) {
            predicates.add(cb.like(
                    cb.lower(root.get("reason")),
                    "%" + criteria.getReason().toLowerCase() + "%"
            ));
        }

        // 👤 Usuario
        if (criteria.getUserId() != null) {
            predicates.add(cb.equal(root.get("userId"), criteria.getUserId()));
        }

        // 📅 Rango de fechas
        if (criteria.getFromDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("transactionDate"), criteria.getFromDate()));
        }

        if (criteria.getToDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("transactionDate"), criteria.getToDate()));
        }
    }
}
