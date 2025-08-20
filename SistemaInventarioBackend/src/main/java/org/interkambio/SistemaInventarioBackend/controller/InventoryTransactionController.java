package org.interkambio.SistemaInventarioBackend.controller;

import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.InventoryTransactionDTO;
import org.interkambio.SistemaInventarioBackend.criteria.InventoryTransactionSearchCriteria;
import org.interkambio.SistemaInventarioBackend.service.InventoryTransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/inventory-transactions")
@RequiredArgsConstructor
public class InventoryTransactionController {

    private final InventoryTransactionService transactionService;

    @GetMapping
    public Page<InventoryTransactionDTO> searchTransactions(
            @ModelAttribute InventoryTransactionSearchCriteria criteria,
            @PageableDefault(size = 20, sort = "transactionDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return transactionService.searchTransactions(criteria, pageable);
    }

    @PostMapping
    public ResponseEntity<InventoryTransactionDTO> createTransaction(
            @Valid @RequestBody InventoryTransactionDTO dto
    ) {
        InventoryTransactionDTO created = transactionService.createTransaction(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
