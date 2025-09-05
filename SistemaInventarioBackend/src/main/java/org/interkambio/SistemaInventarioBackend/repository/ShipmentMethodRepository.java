package org.interkambio.SistemaInventarioBackend.repository;

import org.interkambio.SistemaInventarioBackend.model.ShipmentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ShipmentMethodRepository extends JpaRepository<ShipmentMethod, Long>, JpaSpecificationExecutor<ShipmentMethod> {
}
