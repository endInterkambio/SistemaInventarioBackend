package org.interkambio.SistemaInventarioBackend.service.impl;

import org.interkambio.SistemaInventarioBackend.DTO.BookStockAdjustmentDTO;
import org.interkambio.SistemaInventarioBackend.mapper.BookStockAdjustmentMapper;
import org.interkambio.SistemaInventarioBackend.model.BookStockAdjustment;
import org.interkambio.SistemaInventarioBackend.repository.BookStockAdjustmentRepository;
import org.interkambio.SistemaInventarioBackend.service.BookStockAdjustmentService;
import org.interkambio.SistemaInventarioBackend.criteria.BookStockAdjustmentSearchCriteria;
import org.interkambio.SistemaInventarioBackend.specification.BookStockAdjustmentSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookStockAdjustmentServiceImpl implements BookStockAdjustmentService {

    private final BookStockAdjustmentRepository repository;
    private final BookStockAdjustmentMapper mapper;

    public BookStockAdjustmentServiceImpl(BookStockAdjustmentRepository repository,
                                          BookStockAdjustmentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<BookStockAdjustmentDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public Optional<BookStockAdjustmentDTO> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    @Override
    public BookStockAdjustmentDTO save(BookStockAdjustmentDTO dto) {
        BookStockAdjustment entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public List<BookStockAdjustmentDTO> saveAll(List<BookStockAdjustmentDTO> list) {
        List<BookStockAdjustment> entities = list.stream()
                .map(mapper::toEntity)
                .toList();
        return repository.saveAll(entities).stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public Optional<BookStockAdjustmentDTO> update(Long id, BookStockAdjustmentDTO dto) {
        return repository.findById(id).map(existing -> {
            dto.setId(id);
            BookStockAdjustment updated = mapper.toEntity(dto);
            return mapper.toDTO(repository.save(updated));
        });
    }

    @Override
    public Optional<BookStockAdjustmentDTO> partialUpdate(Long id, Map<String, Object> updates) {
        return repository.findById(id).map(existing -> {
            updates.forEach((key, value) -> {
                switch (key) {
                    case "adjustmentQuantity" -> existing.setAdjustmentQuantity((Integer) value);
                    case "reason" -> existing.setReason((String) value);
                    case "performedAt" -> existing.setPerformedAt((java.time.LocalDateTime) value);
                    // ⚠ Si se quiere actualizar relaciones, aquí habría que mapear IDs a entidades
                }
            });
            return mapper.toDTO(repository.save(existing));
        });
    }

    @Override
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Page<BookStockAdjustmentDTO> searchAdjustments(BookStockAdjustmentSearchCriteria criteria, Pageable pageable) {
        return repository.findAll(BookStockAdjustmentSpecification.withFilters(criteria), pageable)
                .map(mapper::toDTO);
    }
}
