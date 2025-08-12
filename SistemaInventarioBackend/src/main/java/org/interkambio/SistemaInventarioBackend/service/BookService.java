package org.interkambio.SistemaInventarioBackend.service;

import org.interkambio.SistemaInventarioBackend.DTO.BookDTO;
import org.interkambio.SistemaInventarioBackend.criteria.BookSearchCriteria;
import org.interkambio.SistemaInventarioBackend.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookService extends GenericService<BookDTO, Long> {
    Page<BookDTO> findAllBooks(Pageable pageable);
    Optional<Book> findBySku(String sku);
    Optional<BookDTO> partialUpdate(Long id, Map<String, Object> updates);
    Page<BookDTO> searchBooks(BookSearchCriteria criteria, Pageable pageable);
    List<BookDTO> importBooksFromFile(MultipartFile file) throws Exception;
}
