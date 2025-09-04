package org.interkambio.SistemaInventarioBackend.service;

import org.interkambio.SistemaInventarioBackend.DTO.sales.CustomerDTO;
import org.interkambio.SistemaInventarioBackend.criteria.CustomerSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService extends GenericService<CustomerDTO, Long> {
    Page<CustomerDTO> findAll(Pageable pageable);
    Page<CustomerDTO> searchCustomers(CustomerSearchCriteria criteria, int page, int size);
}
