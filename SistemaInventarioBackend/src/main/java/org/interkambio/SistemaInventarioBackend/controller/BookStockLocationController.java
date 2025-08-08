package org.interkambio.SistemaInventarioBackend.controller;

import org.interkambio.SistemaInventarioBackend.DTO.BookStockLocationDTO;
import org.interkambio.SistemaInventarioBackend.service.BookStockLocationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/book-stock-locations")
public class BookStockLocationController {

    private final BookStockLocationService locationService;

    public BookStockLocationController(BookStockLocationService locationService) {
        this.locationService = locationService;
    }

    /*@GetMapping
    public Page<BookStockLocationDTO> getLocations(
            @ModelAttribute BookStockLocationSearchCriteria criteria,
            Pageable pageable
    ) {
        return locationService.searchLocations(criteria, pageable);
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<BookStockLocationDTO> getById(@PathVariable Long id) {
        return locationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BookStockLocationDTO> create(@RequestBody BookStockLocationDTO dto) {
        return ResponseEntity.ok(locationService.save(dto));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<BookStockLocationDTO>> createBatch(@RequestBody List<BookStockLocationDTO> list) {
        return ResponseEntity.ok(locationService.saveAll(list));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookStockLocationDTO> update(
            @PathVariable Long id,
            @RequestBody BookStockLocationDTO dto
    ) {
        return locationService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookStockLocationDTO> partialUpdate(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates
    ) {
        return locationService.partialUpdate(id, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boolean deleted = locationService.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Ubicación eliminada correctamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

