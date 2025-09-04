package org.interkambio.SistemaInventarioBackend.repository;

import org.interkambio.SistemaInventarioBackend.DTO.inventory.WarehouseDTO;
import org.interkambio.SistemaInventarioBackend.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    @Query("SELECT w.name FROM Warehouse w WHERE w.id = :id")
    String findNameById(@Param("id") Long id);

    @Query("SELECT new org.interkambio.SistemaInventarioBackend.DTO.inventory.WarehouseDTO(" +
            "w.id, w.name, w.location, w.description, COALESCE(SUM(bs.stock), 0)) " +
            "FROM Warehouse w LEFT JOIN w.stockLocations bs " +
            "GROUP BY w.id, w.name, w.location, w.description")
    List<WarehouseDTO> findAllWithTotalBooks();
}
