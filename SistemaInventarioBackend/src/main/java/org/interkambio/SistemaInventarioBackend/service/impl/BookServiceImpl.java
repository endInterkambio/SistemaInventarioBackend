
package org.interkambio.SistemaInventarioBackend.service.impl;

import jakarta.transaction.Transactional;
import org.interkambio.SistemaInventarioBackend.DTO.BookDTO;
import org.interkambio.SistemaInventarioBackend.DTO.BookStockLocationDTO;
import org.interkambio.SistemaInventarioBackend.DTO.ImportResult;
import org.interkambio.SistemaInventarioBackend.DTO.SimpleIdNameDTO;
import org.interkambio.SistemaInventarioBackend.criteria.BookSearchCriteria;
import org.interkambio.SistemaInventarioBackend.exporter.BookExcelExporter;
import org.interkambio.SistemaInventarioBackend.importer.UnifiedBookImporter;
import org.interkambio.SistemaInventarioBackend.mapper.BookMapper;
import org.interkambio.SistemaInventarioBackend.model.*;
import org.interkambio.SistemaInventarioBackend.repository.BookRepository;
import org.interkambio.SistemaInventarioBackend.repository.BookStockLocationRepository;
import org.interkambio.SistemaInventarioBackend.repository.UserRepository;
import org.interkambio.SistemaInventarioBackend.repository.WarehouseRepository;
import org.interkambio.SistemaInventarioBackend.security.CustomUserPrincipal;
import org.interkambio.SistemaInventarioBackend.service.BookService;
import org.interkambio.SistemaInventarioBackend.specification.BookSpecification;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl extends GenericServiceImpl<Book, BookDTO, Long> implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final UnifiedBookImporter bookImporter;
    private final BookStockLocationRepository stockLocationRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;

    public BookServiceImpl(
            BookRepository bookRepository,
            BookMapper bookMapper,
            UnifiedBookImporter bookImporter,
            BookStockLocationRepository stockLocationRepository,
            WarehouseRepository warehouseRepository,
            UserRepository userRepository
    ) {
        super(bookRepository, bookMapper); // este es el constructor de GenericServiceImpl
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.bookImporter = bookImporter;
        this.stockLocationRepository = stockLocationRepository;
        this.warehouseRepository = warehouseRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected void setId(Book entity, Long id) {
        entity.setId(id);
    }

    @Override
    public Optional<BookDTO> findBySku(String sku) {
        return bookRepository.findBySku(sku)
                .map(bookMapper::toDTO);
    }

    @Override
    public Page<BookDTO> searchBooks(BookSearchCriteria criteria, Pageable pageable) {
        Specification<Book> specification = BookSpecification.withFilters(criteria);

        Pageable sortedPageable = pageable;
        if (criteria.getSortBy() != null && !criteria.getSortBy().isEmpty()) {
            Sort.Direction direction = "desc".equalsIgnoreCase(criteria.getSortDirection())
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            sortedPageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(direction, criteria.getSortBy())
            );
        }

        Page<Book> bookPage = bookRepository.findAll(specification, sortedPageable);
        return bookPage.map(bookMapper::toDTO);
    }


    @Override
    public BookDTO save(BookDTO dto) {
        if (dto.getSku() != null && bookRepository.existsBySku(dto.getSku())) {
            throw new IllegalArgumentException("Ya existe un libro con el SKU: " + dto.getSku());
        }
        return super.save(dto);
    }

    @Override
    @Transactional
    public List<BookDTO> saveAll(List<BookDTO> books) {
        List<BookDTO> savedBooks = new ArrayList<>();

        for (BookDTO dto : books) {
            try {
                if (dto.getSku() != null && bookRepository.existsBySku(dto.getSku())) {
                    throw new IllegalArgumentException("Ya existe un libro con el SKU: " + dto.getSku());
                }

                // Guardar libro
                Book book = bookMapper.toEntity(dto);
                book = bookRepository.save(book);

                // Guardar ubicaciones solo si existen
                if (dto.getLocations() != null && !dto.getLocations().isEmpty()) {
                    for (BookStockLocationDTO locDTO : dto.getLocations()) {
                        if (locDTO.getWarehouse() == null) {
                            continue; // evita nullpointer si en Excel no había warehouse
                        }

                        BookStockLocation loc = new BookStockLocation();
                        loc.setBook(book);
                        loc.setWarehouse(
                                warehouseRepository.findById(locDTO.getWarehouse().getId())
                                        .orElseThrow(() -> new IllegalArgumentException(
                                                "Almacén no encontrado con ID: " + locDTO.getWarehouse().getId()
                                        ))
                        );
                        loc.setBookcase(locDTO.getBookcase());
                        loc.setBookcaseFloor(locDTO.getBookcaseFloor());
                        loc.setStock(locDTO.getStock() != null ? locDTO.getStock() : 0);
                        if (locDTO.getBookCondition() != null) {
                            loc.setBookCondition(BookCondition.valueOf(locDTO.getBookCondition()));
                        }
                        if (locDTO.getLocationType() != null) {
                            loc.setLocationType(LocationType.valueOf(locDTO.getLocationType()));
                        }
                        stockLocationRepository.save(loc);
                    }
                }

                savedBooks.add(bookMapper.toDTO(book));
            } catch (Exception e) {
                System.err.println("Error al guardar libro con SKU " + dto.getSku() + ": " + e.getMessage());
            }
        }

        return savedBooks;
    }

    @Override
    public Page<BookDTO> findAllBooks(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable); // paginado y con relaciones
        return books.map(bookMapper::toDTO);
    }

    @Override
    @Transactional
    public Optional<BookDTO> partialUpdate(Long id, Map<String, Object> updates) {
        return bookRepository.findById(id).map(existing -> {
            BeanWrapper wrapper = new BeanWrapperImpl(existing);

            // Actualización de campos
            updates.forEach((key, value) -> {
                try {
                    switch (key) {
                        case "categories":
                            if (value instanceof List<?> listValue) {
                                wrapper.setPropertyValue("category",
                                        listValue.stream()
                                                .map(Object::toString)
                                                .collect(Collectors.joining(",")));
                            } else if (value instanceof String strValue) {
                                wrapper.setPropertyValue("category", strValue);
                            }
                            break;

                        case "formats":
                            if (value instanceof List<?> listValue) {
                                wrapper.setPropertyValue("format",
                                        listValue.stream()
                                                .map(Object::toString)
                                                .collect(Collectors.joining(",")));
                            } else if (value instanceof String strValue) {
                                wrapper.setPropertyValue("format", strValue);
                            }
                            break;

                        default:
                            // Para el resto de campos, BeanWrapper usará el setter correspondiente
                            wrapper.setPropertyValue(key, value);
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                            "No se pudo actualizar la propiedad '" + key + "': " + e.getMessage()
                    );
                }
            });

            // Actualizamos la fecha de modificación
            existing.setUpdatedAt(LocalDateTime.now());

            // Asignamos el usuario logueado como responsable de la actualización
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof CustomUserPrincipal customUser) {
                    User userEntity = userRepository.findById(customUser.getId())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Usuario logueado no encontrado: " + customUser.getId()));
                    existing.setUpdatedBy(userEntity);
                }
            }

            // Guardamos la entidad
            Book saved = bookRepository.save(existing);

            // Retornamos el DTO mapeado
            return bookMapper.toDTO(saved);
        });
    }


    @Override
    public ImportResult<BookDTO> importBooksFromFile(MultipartFile file) {
        ImportResult<BookDTO> result = new ImportResult<>();
        try {
            List<BookDTO> books = bookImporter.parse(file);

            if (books == null || books.isEmpty()) {
                result.setSuccess(false);
                result.setMessage("El archivo no contiene libros válidos.");
                return result;
            }

            // Validar SKUs
            for (BookDTO dto : books) {
                if (dto.getSku() == null || dto.getSku().isBlank()) {
                    result.setSuccess(false);
                    result.setMessage("El libro no tiene un SKU definido.");
                    return result;
                }
                if (bookRepository.existsBySku(dto.getSku())) {
                    result.setSuccess(false);
                    result.setMessage("Ya existe un libro con el SKU: " + dto.getSku());
                    return result;
                }
            }

            // Guardar en lote
            List<BookDTO> savedBooks = saveAll(books);
            result.setSuccess(true);
            result.setMessage("Libros importados correctamente.");
            result.setData(savedBooks);
            return result;

        } catch (Exception ex) {
            result.setSuccess(false);
            result.setMessage("Error al importar: " + ex.getMessage());
            return result;
        }
    }


    @Override
    public List<BookStockLocationDTO> getAllStockLocationsDTO() {
        List<BookStockLocation> locations = stockLocationRepository.findAll();

        return locations.stream().map(loc -> {
            BookStockLocationDTO dto = new BookStockLocationDTO();
            dto.setBookSku(loc.getBook().getSku());
            dto.setWarehouse(new SimpleIdNameDTO(loc.getWarehouse().getId(), loc.getWarehouse().getName()));
            dto.setBookcase(loc.getBookcase());
            dto.setBookcaseFloor(loc.getBookcaseFloor());
            dto.setStock(loc.getStock());
            dto.setBookCondition(loc.getBookCondition().name());
            dto.setLocationType(loc.getLocationType().name());
            return dto;
        }).toList();
    }

    @Override
    public void exportBooksWithStock(java.io.OutputStream os) throws Exception {
        List<BookDTO> books = this.findAllBooks(Pageable.unpaged()).getContent();
        List<BookStockLocationDTO> stockLocations = this.getAllStockLocationsDTO();
        BookExcelExporter.exportUnifiedExcel(books, stockLocations, os);
    }
}
