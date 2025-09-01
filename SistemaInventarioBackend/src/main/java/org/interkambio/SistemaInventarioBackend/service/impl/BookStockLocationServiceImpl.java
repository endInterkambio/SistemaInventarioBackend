package org.interkambio.SistemaInventarioBackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.interkambio.SistemaInventarioBackend.DTO.BookStockLocationDTO;
import org.interkambio.SistemaInventarioBackend.criteria.BookStockLocationSearchCriteria;
import org.interkambio.SistemaInventarioBackend.exception.LocationDeleteException;
import org.interkambio.SistemaInventarioBackend.mapper.BookStockLocationMapper;
import org.interkambio.SistemaInventarioBackend.model.Book;
import org.interkambio.SistemaInventarioBackend.model.BookCondition;
import org.interkambio.SistemaInventarioBackend.model.BookStockLocation;
import org.interkambio.SistemaInventarioBackend.model.LocationType;
import org.interkambio.SistemaInventarioBackend.repository.BookRepository;
import org.interkambio.SistemaInventarioBackend.repository.BookStockLocationRepository;
import org.interkambio.SistemaInventarioBackend.repository.InventoryTransactionRepository;
import org.interkambio.SistemaInventarioBackend.service.BookStockLocationService;
import org.interkambio.SistemaInventarioBackend.specification.BookStockLocationSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookStockLocationServiceImpl
        extends GenericServiceImpl<BookStockLocation, BookStockLocationDTO, Long>
        implements BookStockLocationService {

    private final BookStockLocationRepository repository;
    private final BookStockLocationMapper mapper;
    private final BookRepository bookRepository;
    private final InventoryTransactionRepository transactionRepository;

    public BookStockLocationServiceImpl(BookStockLocationRepository repository,
                                        BookRepository bookRepository,
                                        BookStockLocationMapper mapper,
                                        InventoryTransactionRepository transactionRepository) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.bookRepository = bookRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    protected void setId(BookStockLocation entity, Long id) {
        entity.setId(id);
    }

    @Override
    @Transactional
    public BookStockLocationDTO create(BookStockLocationDTO dto) {
        BookStockLocation entity = mapper.toEntity(dto);

        // Cargar libro por ID
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + dto.getBookId()));
        entity.setBook(book);

        // Validación: evitar duplicados en SHOWROOM
        if (entity.getWarehouse().getId() == 1 && entity.getLocationType() == LocationType.SHOWROOM) {
            boolean exists = repository.existsByBookIdAndBookConditionAndWarehouseIdAndLocationType(
                    entity.getBook().getId(),
                    entity.getBookCondition(),
                    entity.getWarehouse().getId(),
                    entity.getLocationType()
            );

            if (exists) {
                throw new RuntimeException(
                        "Ya existe este libro en SHOWROOM con la misma condición. No se permite duplicar."
                );
            }
        }

        BookStockLocation saved = repository.save(entity);

        BookStockLocation reloaded = repository.findById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));

        return mapper.toDTO(reloaded);
    }



    @Override
    public BookStockLocationDTO save(BookStockLocationDTO dto) {
        BookStockLocation entity = mapper.toEntity(dto);

        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + dto.getBookId()));
        entity.setBook(book);

        // Validación SHOWROOM
        if (entity.getWarehouse().getId() == 1 && entity.getLocationType() == LocationType.SHOWROOM) {
            boolean exists = repository.existsByBookIdAndBookConditionAndWarehouseIdAndLocationType(
                    entity.getBook().getId(),
                    entity.getBookCondition(),
                    entity.getWarehouse().getId(),
                    entity.getLocationType()
            );

            if (exists) {
                throw new RuntimeException(
                        "Ya existe este libro en SHOWROOM con la misma condición. No se permite duplicar."
                );
            }
        }

        BookStockLocation saved = repository.save(entity);

        BookStockLocation reloaded = repository.findById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));

        return mapper.toDTO(reloaded);
    }


    @Override
    public List<BookStockLocationDTO> saveAll(List<BookStockLocationDTO> dtoList) {
        List<BookStockLocation> entities = dtoList.stream().map(dto -> {
            BookStockLocation entity = mapper.toEntity(dto);
            Book book = bookRepository.findById(dto.getBookId())
                    .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + dto.getBookId()));
            entity.setBook(book);

            if (entity.getWarehouse().getId() == 1 && entity.getLocationType() == LocationType.SHOWROOM) {
                boolean exists = repository.existsByBookIdAndBookConditionAndWarehouseIdAndLocationType(
                        entity.getBook().getId(),
                        entity.getBookCondition(),
                        entity.getWarehouse().getId(),
                        entity.getLocationType()
                );

                if (exists) {
                    throw new RuntimeException(
                            "Ya existe este libro en SHOWROOM con la misma condición. No se permite duplicar."
                    );
                }
            }

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

            BookStockLocation saved = repository.save(updated);
            BookStockLocation reloaded = repository.findById(saved.getId())
                    .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));
            return mapper.toDTO(reloaded);
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
                    } else {
                        if (field.getType() == Integer.class || field.getType() == int.class) {
                            field.set(entity, Integer.valueOf(value.toString()));
                        } else if (field.getType() == String.class) {
                            field.set(entity, value.toString());
                        } else if (field.getType() == LocalDateTime.class) {
                            field.set(entity, LocalDateTime.parse(value.toString()));
                        } else {
                            field.set(entity, value);
                        }
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    // Ignorar campos inexistentes o inaccesibles
                }
            });

            BookStockLocation saved = repository.save(entity);

            BookStockLocation reloaded = repository.findById(saved.getId())
                    .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));

            return mapper.toDTO(reloaded);
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
