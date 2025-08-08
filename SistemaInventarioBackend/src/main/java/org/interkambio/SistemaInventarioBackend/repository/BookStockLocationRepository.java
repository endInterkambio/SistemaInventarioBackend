package org.interkambio.SistemaInventarioBackend.repository;

import org.interkambio.SistemaInventarioBackend.model.BookStockLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookStockLocationRepository extends JpaRepository<BookStockLocation, Long> {
}
