package org.interkambio.SistemaInventarioBackend.specification;

import org.interkambio.SistemaInventarioBackend.criteria.ShipmentSearchCriteria;
import org.interkambio.SistemaInventarioBackend.model.Shipment;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ShipmentSpecification {

    public static Specification<Shipment> withFilters(ShipmentSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getTrackingNumber() != null && !criteria.getTrackingNumber().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("trackingNumber")),
                        "%" + criteria.getTrackingNumber().toLowerCase() + "%"));
            }

            if (criteria.getAddress() != null && !criteria.getAddress().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("address")),
                        "%" + criteria.getAddress().toLowerCase() + "%"));
            }

            if (criteria.getShipmentDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("shipmentDate"), criteria.getShipmentDateFrom()));
            }

            if (criteria.getShipmentDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("shipmentDate"), criteria.getShipmentDateTo()));
            }

            if (criteria.getMinShippingFee() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("shippingFee"), criteria.getMinShippingFee()));
            }

            if (criteria.getMaxShippingFee() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("shippingFee"), criteria.getMaxShippingFee()));
            }

            if (criteria.getOrderId() != null) {
                predicates.add(cb.equal(root.get("order").get("id"), criteria.getOrderId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
