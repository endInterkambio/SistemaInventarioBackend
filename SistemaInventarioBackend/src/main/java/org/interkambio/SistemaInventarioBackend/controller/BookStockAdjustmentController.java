package org.interkambio.SistemaInventarioBackend.controller;

import org.interkambio.SistemaInventarioBackend.DTO.BookStockAdjustmentDTO;
import org.interkambio.SistemaInventarioBackend.criteria.BookStockAdjustmentSearchCriteria;
import org.interkambio.SistemaInventarioBackend.service.BookStockAdjustmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/book-stock-adjustments")
public class BookStockAdjustmentController {

    private final BookStockAdjustmentService adjustmentService;

    public BookStockAdjustmentController(BookStockAdjustmentService adjustmentService) {
        this.adjustmentService = adjustmentService;
    }

    @GetMapping
    public Page<BookStockAdjustmentDTO> searchAdjustments(
            @ModelAttribute BookStockAdjustmentSearchCriteria criteria,
            Pageable pageable
    ) {
        return adjustmentService.searchAdjustments(criteria, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookStockAdjustmentDTO> getById(@PathVariable Long id) {
        return adjustmentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BookStockAdjustmentDTO> create(@RequestBody BookStockAdjustmentDTO dto) {
        return ResponseEntity.ok(adjustmentService.save(dto));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<BookStockAdjustmentDTO>> createBatch(@RequestBody List<BookStockAdjustmentDTO> list) {
        return ResponseEntity.ok(adjustmentService.saveAll(list));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookStockAdjustmentDTO> update(
            @PathVariable Long id,
            @RequestBody BookStockAdjustmentDTO dto
    ) {
        return adjustmentService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookStockAdjustmentDTO> partialUpdate(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates
    ) {
        return adjustmentService.partialUpdate(id, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boolean deleted = adjustmentService.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Ajuste eliminado correctamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
