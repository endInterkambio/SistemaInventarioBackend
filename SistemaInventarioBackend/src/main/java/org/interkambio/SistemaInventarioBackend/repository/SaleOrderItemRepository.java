package org.interkambio.SistemaInventarioBackend.repository;

import org.interkambio.SistemaInventarioBackend.model.SaleOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleOrderItemRepository extends JpaRepository<SaleOrderItem, Long> {

}
