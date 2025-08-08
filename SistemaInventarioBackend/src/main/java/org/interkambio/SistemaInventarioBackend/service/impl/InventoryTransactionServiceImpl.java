package org.interkambio.SistemaInventarioBackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.InventoryTransactionDTO;
import org.interkambio.SistemaInventarioBackend.model.InventoryTransaction;
import org.interkambio.SistemaInventarioBackend.mapper.InventoryTransactionMapper;
import org.interkambio.SistemaInventarioBackend.repository.InventoryTransactionRepository;
import org.interkambio.SistemaInventarioBackend.service.InventoryTransactionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryTransactionServiceImpl implements InventoryTransactionService {

    private final InventoryTransactionRepository transactionRepository;
    private final InventoryTransactionMapper transactionMapper;

    @Override
    public InventoryTransactionDTO createTransaction(InventoryTransactionDTO dto) {
        InventoryTransaction entity = transactionMapper.toEntity(dto);
        InventoryTransaction saved = transactionRepository.save(entity);
        return transactionMapper.toDTO(saved);
    }

    @Override
    public List<InventoryTransactionDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(transactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryTransactionDTO getTransactionById(Long id) {
        InventoryTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
        return transactionMapper.toDTO(transaction);
    }
}
