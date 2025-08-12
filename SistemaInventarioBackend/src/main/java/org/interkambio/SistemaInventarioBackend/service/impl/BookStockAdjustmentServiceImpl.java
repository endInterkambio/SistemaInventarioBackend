package org.interkambio.SistemaInventarioBackend.service.impl;

import org.interkambio.SistemaInventarioBackend.DTO.BookStockAdjustmentDTO;
import org.interkambio.SistemaInventarioBackend.mapper.BookStockAdjustmentMapper;
import org.interkambio.SistemaInventarioBackend.model.BookStockAdjustment;
import org.interkambio.SistemaInventarioBackend.model.BookStockLocation;
import org.interkambio.SistemaInventarioBackend.repository.BookStockAdjustmentRepository;
import org.interkambio.SistemaInventarioBackend.repository.BookStockLocationRepository;
import org.interkambio.SistemaInventarioBackend.service.BookStockAdjustmentService;
import org.interkambio.SistemaInventarioBackend.criteria.BookStockAdjustmentSearchCriteria;
import org.interkambio.SistemaInventarioBackend.specification.BookStockAdjustmentSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookStockAdjustmentServiceImpl implements BookStockAdjustmentService {

    private final BookStockAdjustmentRepository repository;
    private final BookStockAdjustmentMapper mapper;
    private final BookStockLocationRepository locationRepository;

    public BookStockAdjustmentServiceImpl(BookStockAdjustmentRepository repository,
                                          BookStockAdjustmentMapper mapper,
                                          BookStockLocationRepository locationRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.locationRepository = locationRepository;
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

    public BookStockAdjustmentDTO save(BookStockAdjustmentDTO dto) {
        // 1️⃣ Buscar ubicación
        BookStockLocation location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada: " + dto.getLocationId()));

        // 2️⃣ Actualizar stock
        int newStock = location.getStock() + dto.getAdjustmentQuantity();
        if (newStock < 0) {
            throw new RuntimeException("El stock no puede ser negativo. Stock actual: "
                    + location.getStock());
        }
        location.setStock(newStock);
        locationRepository.save(location);

        // 3️⃣ Crear y guardar ajuste
        BookStockAdjustment entity = mapper.toEntity(dto);

        BookStockAdjustment savedAdjustment = repository.save(entity);

        // 4️⃣ Retornar DTO
        return mapper.toDTO(savedAdjustment);
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
        BookStockAdjustmentSpecification spec = new BookStockAdjustmentSpecification();
        return repository.findAll(spec.build(criteria), pageable)
                .map(mapper::toDTO);
    }
}

