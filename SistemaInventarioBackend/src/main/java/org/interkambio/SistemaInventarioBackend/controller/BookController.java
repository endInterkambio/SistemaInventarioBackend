package org.interkambio.SistemaInventarioBackend.controller;

import org.interkambio.SistemaInventarioBackend.DTO.inventory.BookDTO;
import org.interkambio.SistemaInventarioBackend.criteria.BookSearchCriteria;
import org.interkambio.SistemaInventarioBackend.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Solo lectura para SELLER, todo para ADMIN
    @PreAuthorize("hasAnyAuthority('ADMIN','SELLER')")
    // Buscar por SKU
    @GetMapping("/sku/{sku}")
    public ResponseEntity<BookDTO> getBySku(@PathVariable String sku) {
        return bookService.findBySku(sku)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SELLER')")
    // Obtener todos los libros
    @GetMapping
    public Page<BookDTO> getBooks(
            @ModelAttribute BookSearchCriteria criteria,
            Pageable pageable
    ) {
        return bookService.searchBooks(criteria, pageable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SELLER')")
    // Obtener libro por ID
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Solo ADMIN
    @PreAuthorize("hasAuthority('ADMIN')")
    // Crear un nuevo libro
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.save(bookDTO));
    }

    // Carga de datos masiva
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/batch")
    public ResponseEntity<List<BookDTO>> createBatch(@RequestBody List<BookDTO> books) {
        return ResponseEntity.ok(bookService.saveAll(books));
    }

    // Actualizar todos los campos del libro
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(
            @PathVariable Long id,
            @RequestBody BookDTO bookDTO
    ) {
        return bookService.update(id, bookDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // Actualización parcial
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<BookDTO> partialUpdate(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates
    ) {
        return bookService.partialUpdate(id, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un libro
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        boolean deleted = bookService.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Libro eliminado correctamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportBooks() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bookService.exportBooksWithStock(out);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Books.xlsx");
        headers.add(HttpHeaders.CONTENT_TYPE,
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(out.toByteArray());
    }
}