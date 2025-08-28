package org.interkambio.SistemaInventarioBackend.controller;

import org.interkambio.SistemaInventarioBackend.DTO.CustomerContactDTO;
import org.interkambio.SistemaInventarioBackend.service.CustomerContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer-contacts")
public class CustomerContactController {

    private final CustomerContactService service;

    public CustomerContactController(CustomerContactService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CustomerContactDTO> save(@RequestBody CustomerContactDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<CustomerContactDTO>> saveAll(@RequestBody List<CustomerContactDTO> dtos) {
        return ResponseEntity.ok(service.saveAll(dtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerContactDTO> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CustomerContactDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerContactDTO> update(@PathVariable Long id, @RequestBody CustomerContactDTO dto) {
        return service.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerContactDTO> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return service.partialUpdate(id, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
