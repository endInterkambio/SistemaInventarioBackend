package org.interkambio.SistemaInventarioBackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.sales.PaymentReceivedDTO;
import org.interkambio.SistemaInventarioBackend.mapper.PaymentReceivedMapper;
import org.interkambio.SistemaInventarioBackend.model.PaymentReceived;
import org.interkambio.SistemaInventarioBackend.repository.PaymentReceivedRepository;
import org.interkambio.SistemaInventarioBackend.service.PaymentReceivedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentReceivedServiceImpl implements PaymentReceivedService {

    private final PaymentReceivedRepository repository;
    private final PaymentReceivedMapper mapper;

    @Override
    public PaymentReceivedDTO save(PaymentReceivedDTO dto) {
        PaymentReceived entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public List<PaymentReceivedDTO> saveAll(List<PaymentReceivedDTO> dtos) {
        List<PaymentReceived> entities = dtos.stream()
                .map(mapper::toEntity)
                .toList();
        return repository.saveAll(entities).stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public Optional<PaymentReceivedDTO> findById(Long id) {
        return repository.findById(id).map(mapper::toDTO);
    }

    @Override
    public List<PaymentReceivedDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public Optional<PaymentReceivedDTO> update(Long id, PaymentReceivedDTO dto) {
        return repository.findById(id).map(existing -> {
            PaymentReceived updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            return mapper.toDTO(repository.save(updated));
        });
    }

    @Override
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<PaymentReceivedDTO> partialUpdate(Long id, Map<String, Object> updates) {
        return repository.findById(id).map(entity -> {
            updates.forEach((field, value) -> {
                switch (field) {
                    case "paymentDate" -> entity.setPaymentDate((LocalDateTime) value);
                    case "paymentMethod" -> entity.setPaymentMethod((String) value);
                    case "amount" -> entity.setAmount(new BigDecimal(value.toString()));
                    case "referenceNumber" -> entity.setReferenceNumber((String) value);
                }
            });
            return mapper.toDTO(repository.save(entity));
        });
    }

    @Override
    public List<PaymentReceivedDTO> findBySaleOrderId(Long orderId) {
        return repository.findBySaleOrderId(orderId).stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public Page<PaymentReceivedDTO> findAll(Specification<PaymentReceived> spec, Pageable pageable) {
        return repository.findAll(spec, pageable)
                .map(mapper::toDTO);
    }
}
