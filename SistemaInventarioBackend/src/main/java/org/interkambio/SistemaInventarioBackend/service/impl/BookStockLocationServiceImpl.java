package org.interkambio.SistemaInventarioBackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.interkambio.SistemaInventarioBackend.DTO.inventory.BookStockLocationDTO;
import org.interkambio.SistemaInventarioBackend.criteria.BookStockLocationSearchCriteria;
import org.interkambio.SistemaInventarioBackend.exception.LocationDeleteException;
import org.interkambio.SistemaInventarioBackend.mapper.BookStockLocationMapper;
import org.interkambio.SistemaInventarioBackend.model.*;
import org.interkambio.SistemaInventarioBackend.repository.BookRepository;
import org.interkambio.SistemaInventarioBackend.repository.BookStockLocationRepository;
import org.interkambio.SistemaInventarioBackend.repository.InventoryTransactionRepository;
import org.interkambio.SistemaInventarioBackend.repository.WarehouseRepository;
import org.interkambio.SistemaInventarioBackend.service.BookStockLocationService;
import org.interkambio.SistemaInventarioBackend.specification.BookStockLocationSpecification;
import org.interkambio.SistemaInventarioBackend.util.StockLocationValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookStockLocationServiceImpl
        extends GenericServiceImpl<BookStockLocation, BookStockLocationDTO, Long>
        implements BookStockLocationService {

    private final BookStockLocationRepository repository;
    private final BookStockLocationMapper mapper;
    private final BookRepository bookRepository;
    private final InventoryTransactionRepository transactionRepository;
    private final WarehouseRepository warehouseRepository;

    public BookStockLocationServiceImpl(BookStockLocationRepository repository,
                                        BookRepository bookRepository,
                                        BookStockLocationMapper mapper,
                                        InventoryTransactionRepository transactionRepository,
                                        WarehouseRepository warehouseRepository) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.bookRepository = bookRepository;
        this.transactionRepository = transactionRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    protected void setId(BookStockLocation entity, Long id) {
        entity.setId(id);
    }

    // ----------------- VALIDACIONES -----------------

    private void validateLocationType(BookStockLocation entity) {
        Long warehouseId = entity.getWarehouse().getId();
        LocationType type = entity.getLocationType();

        // Warehouse 1 permite todo
        if (warehouseId == 1) return;

        // Otros warehouses solo MAIN_STORAGE
        if (type != LocationType.MAIN_STORAGE) {
            throw new RuntimeException("El tipo " + type + " no está permitido en este almacén");
        }
    }

    private void validateShowroomDuplicate(BookStockLocation entity) {
        StockLocationValidator.validateShowroomDuplicate(entity, repository);
    }


    private void applyValidations(BookStockLocation entity) {
        validateLocationType(entity);
        validateShowroomDuplicate(entity);
    }

    // ----------------- MÉTODOS CRUD -----------------

    @Override
    @Transactional
    public BookStockLocationDTO create(BookStockLocationDTO dto) {
        BookStockLocation entity = mapper.toEntity(dto);

        // Cargar entidades gestionadas
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + dto.getBookId()));
        entity.setBook(book);

        Warehouse warehouse = warehouseRepository.findById(dto.getWarehouse().getId())
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado con ID: " + dto.getWarehouse().getId()));
        entity.setWarehouse(warehouse);

        applyValidations(entity);

        BookStockLocation saved = repository.save(entity);

        return mapper.toDTO(repository.findById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada")));
    }

    @Override
    public BookStockLocationDTO save(BookStockLocationDTO dto) {
        BookStockLocation entity = mapper.toEntity(dto);

        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + dto.getBookId()));
        entity.setBook(book);

        applyValidations(entity);

        BookStockLocation saved = repository.save(entity);
        return mapper.toDTO(repository.findById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada")));
    }

    @Override
    public List<BookStockLocationDTO> saveAll(List<BookStockLocationDTO> dtoList) {
        List<BookStockLocation> entities = dtoList.stream().map(dto -> {
            BookStockLocation entity = mapper.toEntity(dto);

            Book book = bookRepository.findById(dto.getBookId())
                    .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + dto.getBookId()));
            entity.setBook(book);

            applyValidations(entity);
            return entity;
        }).collect(Collectors.toList());

        List<BookStockLocation> savedEntities = repository.saveAll(entities);
        return savedEntities.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<BookStockLocationDTO> update(Long id, BookStockLocationDTO dto) {
        return repository.findById(id).map(existing -> {
            BookStockLocation updated = mapper.toEntity(dto);

            Book book = bookRepository.findById(dto.getBookId())
                    .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + dto.getBookId()));
            updated.setBook(book);
            updated.setId(id);

            applyValidations(updated);

            BookStockLocation saved = repository.save(updated);
            return mapper.toDTO(repository.findById(saved.getId())
                    .orElseThrow(() -> new RuntimeException("Ubicación no encontrada")));
        });
    }

    @Override
    public Optional<BookStockLocationDTO> partialUpdate(Long id, Map<String, Object> updates) {
        return repository.findById(id).map(entity -> {

            updates.forEach((key, value) -> {
                if (value == null) return;

                try {
                    Field field = BookStockLocation.class.getDeclaredField(key);
                    field.setAccessible(true);

                    if ("locationType".equals(key)) {
                        field.set(entity, LocationType.valueOf(value.toString()));
                    } else if ("bookCondition".equals(key)) {
                        field.set(entity, BookCondition.valueOf(value.toString()));
                    } else if (field.getType() == Integer.class || field.getType() == int.class) {
                        field.set(entity, Integer.valueOf(value.toString()));
                    } else if (field.getType() == String.class) {
                        field.set(entity, value.toString());
                    } else if (field.getType() == LocalDateTime.class && !"lastUpdatedAt".equals(key)) {
                        field.set(entity, LocalDateTime.parse(value.toString()));
                    } else if (field.getType() == OffsetDateTime.class && !"lastUpdatedAt".equals(key)) {
                        field.set(entity, OffsetDateTime.parse(value.toString()));
                    } else {
                        field.set(entity, value);
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    // Ignorar campos inexistentes o inaccesibles
                }
            });

            applyValidations(entity);

            BookStockLocation saved = repository.save(entity);
            return mapper.toDTO(repository.findById(saved.getId())
                    .orElseThrow(() -> new RuntimeException("Ubicación no encontrada")));
        });
    }

    @Override
    public boolean delete(Long id) {
        return repository.findById(id).map(entity -> {
            boolean hasTransactions = transactionRepository.existsByFromLocationId(id) ||
                    transactionRepository.existsByToLocationId(id);

            if (hasTransactions) {
                throw new LocationDeleteException(
                        "No se puede eliminar la ubicación porque está asociada a transacciones de inventario."
                );
            }

            repository.delete(entity);
            return true;
        }).orElseThrow(() -> new EntityNotFoundException(
                "La ubicación con ID " + id + " no existe o ya fue eliminada."
        ));
    }

    @Override
    public Page<BookStockLocationDTO> searchLocations(BookStockLocationSearchCriteria criteria, Pageable pageable) {
        BookStockLocationSpecification spec = new BookStockLocationSpecification();
        return repository.findAll(spec.build(criteria), pageable).map(mapper::toDTO);
    }
}
