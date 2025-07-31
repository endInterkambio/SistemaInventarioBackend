package org.interkambio.SistemaInventarioBackend.controller;

import org.interkambio.SistemaInventarioBackend.DTO.WarehouseDTO;
import org.interkambio.SistemaInventarioBackend.service.WarehouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping
    public List<WarehouseDTO> getAll() {
        return warehouseService.findAll();
    }

    @PostMapping
    public ResponseEntity<WarehouseDTO> create(@RequestBody WarehouseDTO dto) {
        return ResponseEntity.ok(warehouseService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseDTO> update(@PathVariable Long id, @RequestBody WarehouseDTO dto) {
        return warehouseService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boolean deleted = warehouseService.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Eliminado correctamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseDTO> getById(@PathVariable Long id) {
        return warehouseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
