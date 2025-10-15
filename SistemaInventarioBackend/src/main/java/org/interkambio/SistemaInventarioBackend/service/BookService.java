package org.interkambio.SistemaInventarioBackend.service;

import org.interkambio.SistemaInventarioBackend.DTO.inventory.BookDTO;
import org.interkambio.SistemaInventarioBackend.DTO.inventory.BookStockLocationDTO;
import org.interkambio.SistemaInventarioBackend.DTO.common.ImportResult;
import org.interkambio.SistemaInventarioBackend.criteria.BookSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookService extends GenericService<BookDTO, Long> {
    Page<BookDTO> findAllBooks(Pageable pageable);

    Optional<BookDTO> findBySku(String sku);

    Optional<BookDTO> findById(Long id);

    Optional<BookDTO> partialUpdate(Long id, Map<String, Object> updates);

    Page<BookDTO> searchBooks(BookSearchCriteria criteria, Pageable pageable);

    ImportResult<BookDTO> importBooksFromFile(MultipartFile file) throws Exception;

    List<BookStockLocationDTO> getAllStockLocationsDTO();

    void exportBooksWithStock(OutputStream os) throws Exception;

    void exportBooksWithBestStock(OutputStream os) throws Exception;

    void deactivateExpiredOffers();
}
