package org.interkambio.SistemaInventarioBackend.repository;

import org.interkambio.SistemaInventarioBackend.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query("SELECT b.id FROM Book b")
    Page<Long> findAllIds(Pageable pageable);

    @Query("""
                SELECT DISTINCT b FROM Book b
                LEFT JOIN FETCH b.createdBy
                LEFT JOIN FETCH b.updatedBy
                LEFT JOIN FETCH b.stockLocations sl
                LEFT JOIN FETCH sl.warehouse
                WHERE b.id IN :ids
            """)
    List<Book> findAllWithRelations(@Param("ids") List<Long> ids);

    Optional<Book> findBySku(String sku);

    boolean existsBySku(String sku);

    // Encuentra ofertas vencidas
    @Query("SELECT b FROM Book b WHERE b.isOfferActive = true AND b.offerEndDate <:now")
    List<Book> findExpiredOffers(@Param("now") LocalDate now);

    // Desactiva ofertas vencidas en lote
    @Modifying
    @Query("UPDATE Book b SET b.isOfferActive = false WHERE b.isOfferActive = true AND b.offerEndDate < :now")
    int deactiveExpiredOffers(@Param("now") LocalDate now);
}
