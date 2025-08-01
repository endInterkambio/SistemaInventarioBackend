package org.interkambio.SistemaInventarioBackend.service.impl;

import jakarta.transaction.Transactional;
import org.interkambio.SistemaInventarioBackend.DTO.BookDTO;
import org.interkambio.SistemaInventarioBackend.importer.BookCsvImporter;
import org.interkambio.SistemaInventarioBackend.importer.BookExcelImporter;
import org.interkambio.SistemaInventarioBackend.importer.UnifiedBookImporter;
import org.interkambio.SistemaInventarioBackend.mapper.BookMapper;
import org.interkambio.SistemaInventarioBackend.model.Book;
import org.interkambio.SistemaInventarioBackend.repository.BookRepository;
import org.interkambio.SistemaInventarioBackend.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class BookServiceImpl extends GenericServiceImpl<Book, BookDTO, Long> implements BookService {

    private final BookRepository bookRepository;
    private final UnifiedBookImporter bookImporter;

    public BookServiceImpl(
            BookRepository bookRepository,
            BookMapper bookMapper,
            UnifiedBookImporter bookImporter
    ) {
        super(bookRepository, bookMapper); // este es el constructor de GenericServiceImpl
        this.bookRepository = bookRepository;
        this.bookImporter = bookImporter;
    }

    @Override
    protected void setId(Book entity, Long id) {
        entity.setId(id);
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

    // Método para importar archivo
    @Override
    public List<BookDTO> importBooksFromFile(MultipartFile file) throws Exception {
        List<BookDTO> books = bookImporter.parse(file); // Usa UnifiedBookImporter
        return saveAll(books); // Guarda en BD si tu GenericServiceImpl lo soporta
    }

}
