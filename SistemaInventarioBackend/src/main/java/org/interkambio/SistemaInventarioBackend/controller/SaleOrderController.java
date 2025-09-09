package org.interkambio.SistemaInventarioBackend.controller;

import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.sales.SaleOrderDTO;
import org.interkambio.SistemaInventarioBackend.criteria.SaleOrderSearchCriteria;
import org.interkambio.SistemaInventarioBackend.model.SaleOrder;
import org.interkambio.SistemaInventarioBackend.service.SaleOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/sale-orders")
@RequiredArgsConstructor
public class SaleOrderController {

    private final SaleOrderService service;

    // 🔹 Listar todas las órdenes paginadas
    @GetMapping
    public ResponseEntity<Page<SaleOrderDTO>> getAllOrders(Pageable pageable) {
        Page<SaleOrderDTO> page = service.findAllOrders(pageable);
        return ResponseEntity.ok(page);
    }

    // 🔹 Obtener orden por ID
    @GetMapping("/{id}")
    public ResponseEntity<SaleOrderDTO> getOrderById(@PathVariable Long id) {
        Optional<SaleOrderDTO> orderOpt = service.findById(id);
        return orderOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 Obtener orden por número de orden
    @GetMapping("/by-number/{orderNumber}")
    public ResponseEntity<SaleOrderDTO> getOrderByNumber(@PathVariable String orderNumber) {
        Optional<SaleOrderDTO> orderOpt = service.findByOrderNumber(orderNumber);
        return orderOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 Crear nueva orden
    @PostMapping
    public ResponseEntity<SaleOrderDTO> createOrder(@RequestBody SaleOrderDTO orderDTO) {
        SaleOrderDTO saved = service.save(orderDTO);
        return ResponseEntity.ok(saved);
    }

    // 🔹 Obtener el próximo número de orden
    @GetMapping("/next-order-number")
    public ResponseEntity<String> getNextOrderNumber() {
        String next = service.getNextOrderNumber();
        return ResponseEntity.ok(next);
    }

    // 🔹 Actualización parcial
    @PatchMapping("/{id}")
    public ResponseEntity<SaleOrderDTO> partialUpdate(@PathVariable Long id,
                                                      @RequestBody Map<String, Object> updates) {
        Optional<SaleOrderDTO> updated = service.partialUpdate(id, updates);
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 Búsqueda avanzada
    @PostMapping("/search")
    public ResponseEntity<Page<SaleOrderDTO>> searchOrders(@RequestBody SaleOrderSearchCriteria criteria,
                                                           Pageable pageable) {
        Page<SaleOrderDTO> page = service.searchOrders(criteria, pageable);
        return ResponseEntity.ok(page);
    }

    // 🔹 Eliminar orden por ID
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Orden eliminada correctamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

