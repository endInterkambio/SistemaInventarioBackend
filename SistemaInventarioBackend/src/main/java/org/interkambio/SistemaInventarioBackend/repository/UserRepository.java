package org.interkambio.SistemaInventarioBackend.repository;
import org.interkambio.SistemaInventarioBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u.username FROM User u WHERE u.id = :id")
    String findNameById(@Param("id") Long id);

}

