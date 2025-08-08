package org.interkambio.SistemaInventarioBackend.service.impl;
import org.interkambio.SistemaInventarioBackend.DTO.BookStockLocationDTO;
import org.interkambio.SistemaInventarioBackend.mapper.BookStockLocationMapper;
import org.interkambio.SistemaInventarioBackend.model.BookStockLocation;
import org.interkambio.SistemaInventarioBackend.repository.BookStockLocationRepository;
import org.interkambio.SistemaInventarioBackend.service.BookStockLocationService;
import org.springframework.stereotype.Service;

@Service
public class BookStockLocationServiceImpl
        extends GenericServiceImpl<BookStockLocation, BookStockLocationDTO, Long>
        implements BookStockLocationService {

    public BookStockLocationServiceImpl(BookStockLocationRepository repository) {
        super(repository, new BookStockLocationMapper());
    }

    @Override
    protected void setId(BookStockLocation entity, Long id) {
        entity.setId(id);
    }
}

