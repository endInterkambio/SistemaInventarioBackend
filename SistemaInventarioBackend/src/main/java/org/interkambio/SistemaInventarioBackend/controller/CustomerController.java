package org.interkambio.SistemaInventarioBackend.controller;

import jakarta.validation.Valid;
import org.interkambio.SistemaInventarioBackend.DTO.sales.CustomerDTO;
import org.interkambio.SistemaInventarioBackend.criteria.CustomerSearchCriteria;
import org.interkambio.SistemaInventarioBackend.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> save(@Valid @RequestBody CustomerDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<CustomerDTO>> saveAll(@RequestBody List<CustomerDTO> dtos) {
        return ResponseEntity.ok(service.saveAll(dtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> update(@PathVariable Long id, @Valid @RequestBody CustomerDTO dto) {
        return service.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerDTO> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return service.partialUpdate(id, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }

    // Endpoint unificado para listado, búsqueda y filtros
    @GetMapping
    public ResponseEntity<Page<CustomerDTO>> getCustomers(
            @ModelAttribute CustomerSearchCriteria criteria,
            Pageable pageable
    ) {
        Page<CustomerDTO> page = service.searchCustomers(criteria, pageable);
        return ResponseEntity.ok(page);
    }
}
