package org.interkambio.SistemaInventarioBackend.controller;

import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.sales.PaymentReceivedDTO;
import org.interkambio.SistemaInventarioBackend.criteria.PaymentReceivedCriteria;
import org.interkambio.SistemaInventarioBackend.service.PaymentReceivedService;
import org.interkambio.SistemaInventarioBackend.specification.PaymentReceivedSpecification;
import org.interkambio.SistemaInventarioBackend.model.PaymentReceived;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment-received")
@RequiredArgsConstructor
public class PaymentReceivedController {

    private final PaymentReceivedService service;

    @PostMapping
    public ResponseEntity<PaymentReceivedDTO> create(@RequestBody PaymentReceivedDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentReceivedDTO> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentReceivedDTO>> getByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(service.findBySaleOrderId(orderId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PaymentReceivedDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping
    public ResponseEntity<Page<PaymentReceivedDTO>> getPaged(Pageable pageable) {
        Page<PaymentReceivedDTO> page = service.findAll(null, pageable);
        return ResponseEntity.ok(page);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<PaymentReceivedDTO> partialUpdate(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates
    ) {
        return service.partialUpdate(id, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentReceivedDTO> update(
            @PathVariable Long id,
            @RequestBody PaymentReceivedDTO dto
    ) {
        return service.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @PostMapping("/search")
    public ResponseEntity<Page<PaymentReceivedDTO>> search(
            @RequestBody PaymentReceivedCriteria criteria,
            Pageable pageable
    ) {
        Specification<PaymentReceived> spec = PaymentReceivedSpecification.withFilters(criteria);
        Page<PaymentReceivedDTO> page = service.findAll(spec, pageable);
        return ResponseEntity.ok(page);
    }
}
