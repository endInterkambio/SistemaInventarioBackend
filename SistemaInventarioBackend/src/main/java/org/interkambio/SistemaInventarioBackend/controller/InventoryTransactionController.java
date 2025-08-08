package org.interkambio.SistemaInventarioBackend.controller;

import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.InventoryTransactionDTO;
import org.interkambio.SistemaInventarioBackend.service.InventoryTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory-transactions")
@RequiredArgsConstructor
public class InventoryTransactionController {

    private final InventoryTransactionService transactionService;

    @PostMapping
    public ResponseEntity<InventoryTransactionDTO> createTransaction(
            @RequestBody InventoryTransactionDTO dto) {
        InventoryTransactionDTO created = transactionService.createTransaction(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<InventoryTransactionDTO>> getAllTransactions() {
        List<InventoryTransactionDTO> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryTransactionDTO> getTransactionById(@PathVariable Long id) {
        InventoryTransactionDTO transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }
}


