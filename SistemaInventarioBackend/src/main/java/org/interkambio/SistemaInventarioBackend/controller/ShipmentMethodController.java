package org.interkambio.SistemaInventarioBackend.controller;

import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.sales.ShipmentMethodDTO;
import org.interkambio.SistemaInventarioBackend.DTO.sales.ShipmentMethodDTO;
import org.interkambio.SistemaInventarioBackend.criteria.ShipmentMethodSearchCriteria;
import org.interkambio.SistemaInventarioBackend.criteria.ShipmentMethodSearchCriteria;
import org.interkambio.SistemaInventarioBackend.service.ShipmentMethodService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shipment-methods")
@RequiredArgsConstructor
public class ShipmentMethodController {

    private final ShipmentMethodService service;

    @GetMapping
    public ResponseEntity<List<ShipmentMethodDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentMethodDTO> getById(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ShipmentMethodDTO> create(@RequestBody ShipmentMethodDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<ShipmentMethodDTO>> createBulk(
            @RequestBody List<ShipmentMethodDTO> dtos
    ) {
        List<ShipmentMethodDTO> saved = service.saveAll(dtos);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShipmentMethodDTO> update(@PathVariable Long id, @RequestBody ShipmentMethodDTO dto) {
        return service.update(id, dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ShipmentMethodDTO> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return service.partialUpdate(id, updates).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }

    /*@PostMapping("/search")
    public Page<ShipmentMethodDTO> searchShipmentMethods(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ShipmentMethodSearchCriteria criteria = new ShipmentMethodSearchCriteria();
        criteria.setName(name);

        return service.search(criteria, page, size);
    }*/

}
