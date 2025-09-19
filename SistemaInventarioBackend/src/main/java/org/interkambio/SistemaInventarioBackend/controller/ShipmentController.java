package org.interkambio.SistemaInventarioBackend.controller;

import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.sales.ShipmentDTO;
import org.interkambio.SistemaInventarioBackend.criteria.ShipmentSearchCriteria;
import org.interkambio.SistemaInventarioBackend.service.ShipmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService service;

    // ===================== CRUD BÁSICO =====================

    @PostMapping
    public ResponseEntity<ShipmentDTO> create(@RequestBody ShipmentDTO shipmentDTO) {
        return ResponseEntity.ok(service.save(shipmentDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentDTO> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = service.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ShipmentDTO> partialUpdate(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates
    ) {
        return service.partialUpdate(id, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ===================== ENDPOINTS UNIFICADOS =====================

    /**
     * Endpoint unificado para listado, búsqueda y filtros
     */
    @GetMapping
    public ResponseEntity<Page<ShipmentDTO>> getShipments(
            @ModelAttribute ShipmentSearchCriteria criteria,
            Pageable pageable
    ) {
        return ResponseEntity.ok(service.searchShipments(criteria, pageable));
    }

    @GetMapping("/tracking/{trackingNumber}")
    public ResponseEntity<ShipmentDTO> getByTrackingNumber(@PathVariable String trackingNumber) {
        return service.findByTrackingNumber(trackingNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ===================== MÉTODOS NO IMPLEMENTADOS =====================

    @PostMapping("/bulk")
    public ResponseEntity<List<ShipmentDTO>> saveAll(@RequestBody List<ShipmentDTO> dtos) {
        throw new UnsupportedOperationException("saveAll no implementado para Shipment");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShipmentDTO> update(@PathVariable Long id, @RequestBody ShipmentDTO dto) {
        throw new UnsupportedOperationException("update no implementado para Shipment");
    }
}
