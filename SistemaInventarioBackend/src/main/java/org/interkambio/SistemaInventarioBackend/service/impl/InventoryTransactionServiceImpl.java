package org.interkambio.SistemaInventarioBackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.InventoryTransactionDTO;
import org.interkambio.SistemaInventarioBackend.criteria.InventoryTransactionSearchCriteria;
import org.interkambio.SistemaInventarioBackend.mapper.InventoryTransactionMapper;
import org.interkambio.SistemaInventarioBackend.model.InventoryTransaction;
import org.interkambio.SistemaInventarioBackend.repository.InventoryTransactionRepository;
import org.interkambio.SistemaInventarioBackend.service.InventoryTransactionService;
import org.interkambio.SistemaInventarioBackend.specification.InventoryTransactionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryTransactionServiceImpl implements InventoryTransactionService {

    private final InventoryTransactionRepository repository;
    private final InventoryTransactionMapper mapper;

    @Override
    public Page<InventoryTransactionDTO> searchTransactions(InventoryTransactionSearchCriteria criteria, Pageable pageable) {
        InventoryTransactionSpecification spec = new InventoryTransactionSpecification();
        return repository.findAll(spec.build(criteria), pageable)
                .map(mapper::toDTO);
    }
}
