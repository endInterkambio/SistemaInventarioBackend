package org.interkambio.SistemaInventarioBackend.service;

import org.interkambio.SistemaInventarioBackend.DTO.purchase.SupplierDTO;
import org.interkambio.SistemaInventarioBackend.criteria.SupplierSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupplierService extends GenericService<SupplierDTO, Long> {
    Page<SupplierDTO> search(SupplierSearchCriteria criteria, Pageable pageable);
}
