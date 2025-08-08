package org.interkambio.SistemaInventarioBackend.service.impl;

import org.interkambio.SistemaInventarioBackend.DTO.BookStockAdjustmentDTO;
import org.interkambio.SistemaInventarioBackend.mapper.BookStockAdjustmentMapper;
import org.interkambio.SistemaInventarioBackend.model.BookStockAdjustment;
import org.interkambio.SistemaInventarioBackend.repository.BookStockAdjustmentRepository;
import org.interkambio.SistemaInventarioBackend.service.BookStockAdjustmentService;
import org.springframework.stereotype.Service;

@Service
public class BookStockAdjustmentServiceImpl
        extends GenericServiceImpl<BookStockAdjustment, BookStockAdjustmentDTO, Long>
        implements BookStockAdjustmentService {

    public BookStockAdjustmentServiceImpl(BookStockAdjustmentRepository repository) {
        super(repository, new BookStockAdjustmentMapper());
    }

    @Override
    protected void setId(BookStockAdjustment entity, Long id) {
        entity.setId(id);
    }
}

