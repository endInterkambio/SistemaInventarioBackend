package org.interkambio.SistemaInventarioBackend.importer;

import org.interkambio.SistemaInventarioBackend.DTO.inventory.BookDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookFileImporter {
    List<BookDTO> parse(MultipartFile file) throws Exception;
}
