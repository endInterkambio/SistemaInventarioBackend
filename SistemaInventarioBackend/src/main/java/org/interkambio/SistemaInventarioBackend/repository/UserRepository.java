package org.interkambio.SistemaInventarioBackend.repository;
import org.interkambio.SistemaInventarioBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}

