package org.interkambio.SistemaInventarioBackend.service;

import org.interkambio.SistemaInventarioBackend.DTO.BookStockAdjustmentDTO;
import org.interkambio.SistemaInventarioBackend.criteria.BookStockAdjustmentSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookStockAdjustmentService extends GenericService<BookStockAdjustmentDTO, Long> {
    Page<BookStockAdjustmentDTO> searchAdjustments(BookStockAdjustmentSearchCriteria criteria, Pageable pageable);
}
