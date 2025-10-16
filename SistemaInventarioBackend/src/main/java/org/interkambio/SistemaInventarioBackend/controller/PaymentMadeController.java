package org.interkambio.SistemaInventarioBackend.controller;

import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.purchase.PaymentMadeDTO;
import org.interkambio.SistemaInventarioBackend.criteria.PaymentMadeCriteria;
import org.interkambio.SistemaInventarioBackend.model.PaymentMade;
import org.interkambio.SistemaInventarioBackend.service.PaymentMadeService;
import org.interkambio.SistemaInventarioBackend.specification.PaymentMadeSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment-made")
@RequiredArgsConstructor
public class PaymentMadeController {

    private final PaymentMadeService service;

    @PostMapping
    public ResponseEntity<PaymentMadeDTO> create(@RequestBody PaymentMadeDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMadeDTO> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint unificado para listado + búsqueda + filtros
    @GetMapping
    public ResponseEntity<Page<PaymentMadeDTO>> getPayments(
            @ModelAttribute PaymentMadeCriteria criteria,
            Pageable pageable
    ) {
        Specification<PaymentMade> spec = PaymentMadeSpecification.withFilters(criteria);
        Page<PaymentMadeDTO> page = service.findAll(spec, pageable);
        return ResponseEntity.ok(page);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PaymentMadeDTO> partialUpdate(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates
    ) {
        return service.partialUpdate(id, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentMadeDTO> update(
            @PathVariable Long id,
            @RequestBody PaymentMadeDTO dto
    ) {
        return service.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
