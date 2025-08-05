package org.interkambio.SistemaInventarioBackend.repository;

import org.interkambio.SistemaInventarioBackend.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @EntityGraph(attributePaths = {"warehouse", "createdBy", "updatedBy"})
    Page<Book> findAll(Pageable pageable); // Sin filtro

    @EntityGraph(attributePaths = {"warehouse", "createdBy", "updatedBy"})
    Page<Book> findAll(Specification<Book> spec, Pageable pageable);

    boolean existsBySku(String sku);
}
