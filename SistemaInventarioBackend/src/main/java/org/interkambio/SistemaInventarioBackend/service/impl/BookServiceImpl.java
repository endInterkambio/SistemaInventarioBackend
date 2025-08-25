
package org.interkambio.SistemaInventarioBackend.service.impl;

import jakarta.transaction.Transactional;
import org.interkambio.SistemaInventarioBackend.DTO.BookDTO;
import org.interkambio.SistemaInventarioBackend.DTO.BookStockLocationDTO;
import org.interkambio.SistemaInventarioBackend.DTO.SimpleIdNameDTO;
import org.interkambio.SistemaInventarioBackend.criteria.BookSearchCriteria;
import org.interkambio.SistemaInventarioBackend.exporter.BookExcelExporter;
import org.interkambio.SistemaInventarioBackend.importer.UnifiedBookImporter;
import org.interkambio.SistemaInventarioBackend.mapper.BookMapper;
import org.interkambio.SistemaInventarioBackend.model.Book;
import org.interkambio.SistemaInventarioBackend.model.BookStockLocation;
import org.interkambio.SistemaInventarioBackend.repository.BookRepository;
import org.interkambio.SistemaInventarioBackend.repository.BookStockLocationRepository;
import org.interkambio.SistemaInventarioBackend.service.BookService;
import org.interkambio.SistemaInventarioBackend.specification.BookSpecification;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl extends GenericServiceImpl<Book, BookDTO, Long> implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final UnifiedBookImporter bookImporter;
    private final BookStockLocationRepository stockLocationRepository;

    public BookServiceImpl(
            BookRepository bookRepository,
            BookMapper bookMapper,
            UnifiedBookImporter bookImporter,
            BookStockLocationRepository stockLocationRepository
    ) {
        super(bookRepository, bookMapper); // este es el constructor de GenericServiceImpl
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.bookImporter = bookImporter;
        this.stockLocationRepository = stockLocationRepository;
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
        for (BookDTO dto : books) {
            if (dto.getSku() != null && bookRepository.existsBySku(dto.getSku())) {
                throw new IllegalArgumentException("Ya existe un libro con el SKU: " + dto.getSku());
            }
        }
        return super.saveAll(books);
    }

    @Override
    public Page<BookDTO> findAllBooks(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable); // paginado y con relaciones
        return books.map(bookMapper::toDTO);
    }

    @Override
    public Optional<BookDTO> partialUpdate(Long id, Map<String, Object> updates) {
        return bookRepository.findById(id).map(existing -> {
            BeanWrapper wrapper = new BeanWrapperImpl(existing);

            updates.forEach((key, value) -> {
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
            });

            // Actualizamos la fecha de modificación
            existing.setUpdatedAt(LocalDateTime.now());

            // Guardamos la entidad
            Book saved = bookRepository.save(existing);

            // Debug
            System.out.println("categories=" + saved.getCategory());
            System.out.println("formats=" + saved.getFormat());

            // Retornamos el DTO mapeado
            return bookMapper.toDTO(saved);
        });
    }

    // Método para importar archivo
    @Override
    public List<BookDTO> importBooksFromFile(MultipartFile file) throws Exception {
        try {
            List<BookDTO> books = bookImporter.parse(file);
            return saveAll(books);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Error al importar libros: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error general al procesar el archivo. Verifica que los datos sean válidos y el formato correcto.", ex);
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
