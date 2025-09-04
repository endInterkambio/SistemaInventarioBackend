package org.interkambio.SistemaInventarioBackend.service;

import org.interkambio.SistemaInventarioBackend.DTO.sales.ShipmentItemDTO;

import java.util.List;

public interface ShipmentItemService extends GenericService<ShipmentItemDTO, Long> {
    List<ShipmentItemDTO> findByShipmentId(Long shipmentId);
}
