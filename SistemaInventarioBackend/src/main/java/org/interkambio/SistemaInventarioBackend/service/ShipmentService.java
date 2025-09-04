package org.interkambio.SistemaInventarioBackend.service;

import org.interkambio.SistemaInventarioBackend.DTO.sales.ShipmentDTO;
import org.interkambio.SistemaInventarioBackend.criteria.ShipmentSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ShipmentService extends GenericService<ShipmentDTO, Long> {

    Page<ShipmentDTO> findAllShipments(Pageable pageable);

    Optional<ShipmentDTO> findByTrackingNumber(String trackingNumber);

    Page<ShipmentDTO> searchShipments(ShipmentSearchCriteria criteria, Pageable pageable);
}
