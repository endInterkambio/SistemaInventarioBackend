package org.interkambio.SistemaInventarioBackend.service;

import org.interkambio.SistemaInventarioBackend.DTO.BookDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService extends GenericService<BookDTO, Long> {
    List<BookDTO> findAllBooks();
    List<BookDTO> importBooksFromFile(MultipartFile file) throws Exception;
}
