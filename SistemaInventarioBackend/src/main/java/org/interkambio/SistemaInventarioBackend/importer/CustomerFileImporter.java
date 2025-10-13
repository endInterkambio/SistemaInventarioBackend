package org.interkambio.SistemaInventarioBackend.importer;

import org.interkambio.SistemaInventarioBackend.DTO.sales.CustomerDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerFileImporter {
    List<CustomerDTO> parse(MultipartFile file) throws Exception;
}
