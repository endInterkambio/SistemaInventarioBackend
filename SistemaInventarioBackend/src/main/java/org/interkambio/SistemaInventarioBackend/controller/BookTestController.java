package org.interkambio.SistemaInventarioBackend.controller;

import org.interkambio.SistemaInventarioBackend.DTO.BookDTO;
import org.interkambio.SistemaInventarioBackend.mapper.BookMapper;
import org.interkambio.SistemaInventarioBackend.model.Book;
import org.interkambio.SistemaInventarioBackend.repository.BookRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/test-books")
public class BookTestController {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookTestController(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @PatchMapping("/{id}/test-update")
    public ResponseEntity<BookDTO> testPartialUpdate(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates
    ) {
        return bookRepository.findById(id).map(existing -> {

            updates.forEach((key, value) -> {
                switch (key) {
                    case "categories":
                        if (value instanceof List<?> listValue) {
                            existing.setCategory(
                                    listValue.stream()
                                            .map(Object::toString)
                                            .collect(Collectors.joining(","))
                            );
                        }
                        break;

                    case "formats":
                        if (value instanceof List<?> listValue) {
                            existing.setFormat(
                                    listValue.stream()
                                            .map(Object::toString)
                                            .collect(Collectors.joining(","))
                            );
                        }
                        break;

                    case "title":
                        existing.setTitle((String) value);
                        break;

                    case "author":
                        existing.setAuthor((String) value);
                        break;

                    case "isbn":
                        existing.setIsbn((String) value);
                        break;

                    case "publisher":
                        existing.setPublisher((String) value);
                        break;

                    case "description":
                        existing.setDescription((String) value);
                        break;

                    case "language":
                        existing.setLanguage((String) value);
                        break;

                    case "imageUrl":
                        existing.setImageUrl((String) value);
                        break;

                    case "websiteUrl":
                        existing.setWebsiteUrl((String) value);
                        break;

                    case "coverPrice":
                        existing.setCoverPrice(new BigDecimal(value.toString()));
                        break;

                    case "purchasePrice":
                        existing.setPurchasePrice(new BigDecimal(value.toString()));
                        break;

                    case "sellingPrice":
                        existing.setSellingPrice(new BigDecimal(value.toString()));
                        break;

                    case "fairPrice":
                        existing.setFairPrice(new BigDecimal(value.toString()));
                        break;

                    case "tag":
                        existing.setTag((String) value);
                        break;

                    case "filter":
                        existing.setFilter((String) value);
                        break;

                    case "productSaleType":
                        existing.setProductSaleType((String) value);
                        break;

                    // agrega aquí más campos si es necesario

                    default:
                        // ignorar campos no mapeados
                }
            });

            existing.setUpdatedAt(LocalDateTime.now());
            Book saved = bookRepository.save(existing);

            // Debug
            System.out.println("Guardado en DB:");
            System.out.println("category=" + saved.getCategory());
            System.out.println("format=" + saved.getFormat());

            return ResponseEntity.ok(bookMapper.toDTO(saved));
        }).orElse(ResponseEntity.notFound().build());
    }

}

