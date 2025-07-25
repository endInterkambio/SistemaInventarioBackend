package org.interkambio.SistemaInventarioBackend.repository;

import org.interkambio.SistemaInventarioBackend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
