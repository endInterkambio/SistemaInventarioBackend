package org.interkambio.SistemaInventarioBackend.repository;

import org.interkambio.SistemaInventarioBackend.model.CustomerContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerContactRepository extends JpaRepository<CustomerContact, Long>, JpaSpecificationExecutor<CustomerContact> {
}
