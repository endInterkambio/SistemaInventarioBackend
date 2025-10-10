package org.interkambio.SistemaInventarioBackend.service;

import org.interkambio.SistemaInventarioBackend.DTO.common.ImportResult;
import org.interkambio.SistemaInventarioBackend.DTO.sales.CustomerDTO;
import org.interkambio.SistemaInventarioBackend.criteria.CustomerSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;

public interface CustomerService extends GenericService<CustomerDTO, Long> {
    Page<CustomerDTO> findAll(Pageable pageable);
    Page<CustomerDTO> searchCustomers(CustomerSearchCriteria criteria, Pageable pageable);
    ImportResult<CustomerDTO> importCustomersFromFile(MultipartFile file) throws Exception;
    void exportCustomers(OutputStream os) throws Exception;
}
