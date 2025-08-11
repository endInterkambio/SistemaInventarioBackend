
package org.interkambio.SistemaInventarioBackend.service.impl;

import jakarta.transaction.Transactional;
import org.interkambio.SistemaInventarioBackend.DTO.BookDTO;
import org.interkambio.SistemaInventarioBackend.criteria.BookSearchCriteria;
import org.interkambio.SistemaInventarioBackend.importer.UnifiedBookImporter;
import org.interkambio.SistemaInventarioBackend.mapper.BookMapper;
import org.interkambio.SistemaInventarioBackend.model.Book;
import org.interkambio.SistemaInventarioBackend.repository.BookRepository;
import org.interkambio.SistemaInventarioBackend.service.BookService;
import org.interkambio.SistemaInventarioBackend.specification.BookSpecification;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl extends GenericServiceImpl<Book, BookDTO, Long> implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final UnifiedBookImporter bookImporter;

    public BookServiceImpl(
            BookRepository bookRepository,
            BookMapper bookMapper,
            UnifiedBookImporter bookImporter
    ) {
        super(bookRepository, bookMapper); // este es el constructor de GenericServiceImpl
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.bookImporter = bookImporter;
    }

    @Override
    protected void setId(Book entity, Long id) {
        entity.setId(id);
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

        // Paso 1: obtener solo IDs paginados
        Page<Long> idPage = bookRepository.findAll(specification, sortedPageable)
                .map(Book::getId);

        if (idPage.isEmpty()) {
            return Page.empty(sortedPageable);
        }

        // Paso 2: traer los libros con todas las relaciones en 1 query
        List<Book> books = bookRepository.findAllWithRelations(idPage.getContent());

        return new PageImpl<>(books.stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList()),
                sortedPageable,
                idPage.getTotalElements()
        );
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
}
