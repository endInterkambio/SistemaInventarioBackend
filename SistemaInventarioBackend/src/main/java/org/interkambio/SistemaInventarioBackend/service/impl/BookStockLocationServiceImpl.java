package org.interkambio.SistemaInventarioBackend.service.impl;

import org.interkambio.SistemaInventarioBackend.DTO.BookStockLocationDTO;
import org.interkambio.SistemaInventarioBackend.criteria.BookStockLocationSearchCriteria;
import org.interkambio.SistemaInventarioBackend.mapper.BookStockLocationMapper;
import org.interkambio.SistemaInventarioBackend.model.BookCondition;
import org.interkambio.SistemaInventarioBackend.model.BookStockLocation;
import org.interkambio.SistemaInventarioBackend.repository.BookStockLocationRepository;
import org.interkambio.SistemaInventarioBackend.service.BookStockLocationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookStockLocationServiceImpl
        extends GenericServiceImpl<BookStockLocation, BookStockLocationDTO, Long>
        implements BookStockLocationService {

    private final BookStockLocationRepository repository;
    private final BookStockLocationMapper mapper;

    public BookStockLocationServiceImpl(BookStockLocationRepository repository,
                                        BookStockLocationMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    protected void setId(BookStockLocation entity, Long id) {
        entity.setId(id);
    }

    @Override
    public Page<BookStockLocationDTO> searchLocations(BookStockLocationSearchCriteria criteria, Pageable pageable) {
        Specification<BookStockLocation> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // bookSku -> root.book.sku
            if (criteria.getBookSku() != null && !criteria.getBookSku().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("book").get("sku")), "%" + criteria.getBookSku().toLowerCase() + "%"));
            }

            // warehouseId -> root.warehouse.id
            if (criteria.getWarehouseId() != null) {
                predicates.add(cb.equal(root.get("warehouse").get("id"), criteria.getWarehouseId()));
            }

            // bookcase exact match
            if (criteria.getBookcase() != null) {
                predicates.add(cb.equal(root.get("bookcase"), criteria.getBookcase()));
            }

            // bookcaseFloor exact match
            if (criteria.getBookcaseFloor() != null) {
                predicates.add(cb.equal(root.get("bookcaseFloor"), criteria.getBookcaseFloor()));
            }

            // stock range
            if (criteria.getMinStock() != null) {
                predicates.add(cb.ge(root.get("stock"), criteria.getMinStock()));
            }
            if (criteria.getMaxStock() != null) {
                predicates.add(cb.le(root.get("stock"), criteria.getMaxStock()));
            }

            // bookCondition (enum). validamos y comparamos como enum
            if (criteria.getBookCondition() != null && !criteria.getBookCondition().isEmpty()) {
                try {
                    BookCondition cond = BookCondition.valueOf(criteria.getBookCondition());
                    predicates.add(cb.equal(root.get("bookCondition"), cond));
                } catch (IllegalArgumentException ex) {
                    // si valor no válido, no agregamos predicado (o podrías lanzar error)
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return repository.findAll(spec, pageable).map(mapper::toDTO);
    }
}
