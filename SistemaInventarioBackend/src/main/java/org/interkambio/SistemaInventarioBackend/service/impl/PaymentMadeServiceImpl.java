package org.interkambio.SistemaInventarioBackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.purchase.PaymentMadeDTO;
import org.interkambio.SistemaInventarioBackend.mapper.PaymentMadeMapper;
import org.interkambio.SistemaInventarioBackend.model.PaymentMade;
import org.interkambio.SistemaInventarioBackend.model.PurchaseOrder;
import org.interkambio.SistemaInventarioBackend.model.PaymentStatus;
import org.interkambio.SistemaInventarioBackend.model.OrderStatus;
import org.interkambio.SistemaInventarioBackend.repository.PaymentMadeRepository;
import org.interkambio.SistemaInventarioBackend.repository.PurchaseOrderRepository;
import org.interkambio.SistemaInventarioBackend.service.PaymentMadeService;
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
public class PaymentMadeServiceImpl implements PaymentMadeService {

    private final PaymentMadeRepository repository;
    private final PaymentMadeMapper mapper;
    private final PurchaseOrderRepository purchaseOrderRepository;

    @Override
    @Transactional
    public PaymentMadeDTO save(PaymentMadeDTO dto) {
        PaymentMade entity = mapper.toEntity(dto);

        PurchaseOrder order = purchaseOrderRepository.findById(dto.getPurchaseOrderId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "PurchaseOrder con id " + dto.getPurchaseOrderId() + " no encontrado"));

        entity.setPurchaseOrder(order);

        BigDecimal saldoPendiente = order.getTotalAmount().subtract(order.getTotalPaid());

        if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El pago debe ser mayor a 0");
        }
        if (dto.getAmount().compareTo(saldoPendiente) > 0) {
            throw new IllegalArgumentException("El pago excede el saldo pendiente de la orden de compra");
        }

        PaymentMade saved = repository.save(entity);
        updateOrderStatus(order);

        return mapper.toDTO(saved);
    }

    @Override
    public List<PaymentMadeDTO> saveAll(List<PaymentMadeDTO> dtos) {
        List<PaymentMade> entities = dtos.stream()
                .map(mapper::toEntity)
                .toList();
        return repository.saveAll(entities).stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public Optional<PaymentMadeDTO> findById(Long id) {
        return repository.findById(id).map(mapper::toDTO);
    }

    @Override
    public List<PaymentMadeDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional
    public Optional<PaymentMadeDTO> update(Long id, PaymentMadeDTO dto) {
        return repository.findById(id).map(existing -> {
            PurchaseOrder order = existing.getPurchaseOrder();

            existing.setPaymentDate(dto.getPaymentDate());
            existing.setAmount(dto.getAmount());
            existing.setReferenceNumber(dto.getReferenceNumber());

            PaymentMade updated = repository.save(existing);
            updateOrderStatus(order);

            return mapper.toDTO(updated);
        });
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return repository.findById(id).map(existing -> {
            PurchaseOrder order = existing.getPurchaseOrder();
            repository.delete(existing);
            updateOrderStatus(order);
            return true;
        }).orElse(false);
    }

    @Override
    public Optional<PaymentMadeDTO> partialUpdate(Long id, Map<String, Object> updates) {
        return repository.findById(id).map(entity -> {
            updates.forEach((field, value) -> {
                switch (field) {
                    case "paymentDate" -> entity.setPaymentDate((LocalDateTime) value);
                    case "amount" -> entity.setAmount(new BigDecimal(value.toString()));
                    case "referenceNumber" -> entity.setReferenceNumber((String) value);
                }
            });
            return mapper.toDTO(repository.save(entity));
        });
    }

    @Override
    public List<PaymentMadeDTO> findByPurchaseOrderId(Long orderId) {
        return repository.findByPurchaseOrderId(orderId).stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public Page<PaymentMadeDTO> findAll(Specification<PaymentMade> spec, Pageable pageable) {
        return repository.findAll(spec, pageable)
                .map(mapper::toDTO);
    }

    private void updateOrderStatus(PurchaseOrder order) {
        // Calcular total pagado a partir de la BD
        BigDecimal totalPaid = repository.findByPurchaseOrderId(order.getId()).stream()
                .map(p -> p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAmount = order.getTotalAmount();

        if (totalPaid.compareTo(BigDecimal.ZERO) == 0) {
            order.setPaymentStatus(PaymentStatus.UNPAID);
        } else if (totalPaid.compareTo(totalAmount) >= 0) {
            order.setPaymentStatus(PaymentStatus.PAID);
        } else {
            order.setPaymentStatus(PaymentStatus.PARTIALLY_PAID);
        }

        // Actualizar estado general de la orden de compra
        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            order.setStatus(OrderStatus.COMPLETED);
        } else if (order.getStatus() == null || order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.PENDING);
        }

        purchaseOrderRepository.save(order);
    }
}
