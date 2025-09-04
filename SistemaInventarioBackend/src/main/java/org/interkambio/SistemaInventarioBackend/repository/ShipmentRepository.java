package org.interkambio.SistemaInventarioBackend.repository;

import org.interkambio.SistemaInventarioBackend.model.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment, Long>, JpaSpecificationExecutor<Shipment> {

    Optional<Shipment> findByTrackingNumber(String trackingNumber);

    @Query("SELECT s.id FROM Shipment s")
    Page<Long> findAllIds(Pageable pageable);

    @Query("""
            SELECT DISTINCT s FROM Shipment s
            LEFT JOIN FETCH s.order o
            LEFT JOIN FETCH o.customer
            WHERE s.id IN :ids
            """)
    List<Shipment> findAllWithRelations(@Param("ids") List<Long> ids);


}
