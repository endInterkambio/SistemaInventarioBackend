package org.interkambio.SistemaInventarioBackend.service;

import org.interkambio.SistemaInventarioBackend.DTO.BookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService extends GenericService<BookDTO, Long> {
    Page<BookDTO> findAllBooks(Pageable pageable);
    List<BookDTO> importBooksFromFile(MultipartFile file) throws Exception;
}
