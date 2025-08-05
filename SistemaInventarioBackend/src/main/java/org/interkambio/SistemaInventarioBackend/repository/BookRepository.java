package org.interkambio.SistemaInventarioBackend.repository;

import org.interkambio.SistemaInventarioBackend.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(attributePaths = {"warehouse", "createdByUser", "updatedByUser"})
    @Query("SELECT b FROM Book b")
    List<Book> findAllWithRelations();

    boolean existsBySku(String sku);
}
