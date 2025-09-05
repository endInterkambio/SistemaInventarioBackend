package org.interkambio.SistemaInventarioBackend.service;

import org.interkambio.SistemaInventarioBackend.DTO.sales.ShipmentMethodDTO;
import org.interkambio.SistemaInventarioBackend.criteria.ShipmentMethodSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShipmentMethodService extends GenericService<ShipmentMethodDTO, Long> {
    Page<ShipmentMethodDTO> search(ShipmentMethodSearchCriteria criteria, Pageable pageable);

}
