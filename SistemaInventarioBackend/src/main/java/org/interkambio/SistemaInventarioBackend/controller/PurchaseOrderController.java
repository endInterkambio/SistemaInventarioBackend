package org.interkambio.SistemaInventarioBackend.controller;

import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.purchase.PurchaseOrderDTO;
import org.interkambio.SistemaInventarioBackend.criteria.PurchaseOrderSearchCriteria;
import org.interkambio.SistemaInventarioBackend.service.PurchaseOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService service;

    @GetMapping
    public ResponseEntity<Page<PurchaseOrderDTO>> getAllOrders(
            @ModelAttribute PurchaseOrderSearchCriteria criteria,
            Pageable pageable
    ) {
        Page<PurchaseOrderDTO> page = service.searchPurchaseOrders(criteria, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderDTO> getOrderById(@PathVariable Long id) {
        Optional<PurchaseOrderDTO> orderOpt = service.findById(id);
        return orderOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/by-number/{orderNumber}")
    public ResponseEntity<PurchaseOrderDTO> getOrderByNumber(@PathVariable String orderNumber) {
        Optional<PurchaseOrderDTO> orderOpt = service.findByPurchaseOrderNumber(orderNumber);
        return orderOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PurchaseOrderDTO> createOrder(@RequestBody PurchaseOrderDTO dto) {
        PurchaseOrderDTO saved = service.save(dto);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<PurchaseOrderDTO>> createBulk(@RequestBody List<PurchaseOrderDTO> dtos) {
        return ResponseEntity.ok(service.saveAll(dtos));
    }

    @GetMapping("/next-order-number")
    public ResponseEntity<String> getNextOrderNumber() {
        String next = service.getNextOrderNumber();
        return ResponseEntity.ok(next);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PurchaseOrderDTO> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<PurchaseOrderDTO> updated = service.partialUpdate(id, updates);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/search")
    public ResponseEntity<Page<PurchaseOrderDTO>> searchOrders(
            @RequestBody PurchaseOrderSearchCriteria criteria, Pageable pageable
    ) {
        Page<PurchaseOrderDTO> page = service.searchPurchaseOrders(criteria, pageable);
        return ResponseEntity.ok(page);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        boolean deleted = service.delete(id);
        if (deleted) return ResponseEntity.ok("Orden de compra eliminada correctamente.");
        else return ResponseEntity.notFound().build();
    }
}
