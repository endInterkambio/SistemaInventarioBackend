package org.interkambio.SistemaInventarioBackend.specification;

import org.interkambio.SistemaInventarioBackend.criteria.PaymentReceivedCriteria;
import org.interkambio.SistemaInventarioBackend.model.PaymentReceived;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class PaymentReceivedSpecification {

    public static Specification<PaymentReceived> withFilters(PaymentReceivedCriteria criteria) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (Objects.nonNull(criteria.getSaleOrderId())) {
                predicates.getExpressions().add(
                        cb.equal(root.get("saleOrder").get("id"), criteria.getSaleOrderId())
                );
            }

            if (Objects.nonNull(criteria.getPaymentMethod())) {
                predicates.getExpressions().add(
                        cb.like(cb.lower(root.get("paymentMethod")), "%" + criteria.getPaymentMethod().toLowerCase() + "%")
                );
            }

            if (Objects.nonNull(criteria.getPaymentDateFrom())) {
                predicates.getExpressions().add(
                        cb.greaterThanOrEqualTo(root.get("paymentDate"), criteria.getPaymentDateFrom())
                );
            }

            if (Objects.nonNull(criteria.getPaymentDateTo())) {
                predicates.getExpressions().add(
                        cb.lessThanOrEqualTo(root.get("paymentDate"), criteria.getPaymentDateTo())
                );
            }

            if (Objects.nonNull(criteria.getMinAmount())) {
                predicates.getExpressions().add(
                        cb.greaterThanOrEqualTo(root.get("amount"), criteria.getMinAmount())
                );
            }

            if (Objects.nonNull(criteria.getMaxAmount())) {
                predicates.getExpressions().add(
                        cb.lessThanOrEqualTo(root.get("amount"), criteria.getMaxAmount())
                );
            }

            if (Objects.nonNull(criteria.getReferenceNumber())) {
                predicates.getExpressions().add(
                        cb.like(cb.lower(root.get("referenceNumber")), "%" + criteria.getReferenceNumber().toLowerCase() + "%")
                );
            }

            return predicates;
        };
    }
}
