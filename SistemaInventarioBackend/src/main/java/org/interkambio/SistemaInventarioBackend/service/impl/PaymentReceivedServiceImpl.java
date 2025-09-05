package org.interkambio.SistemaInventarioBackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.sales.PaymentReceivedDTO;
import org.interkambio.SistemaInventarioBackend.mapper.PaymentReceivedMapper;
import org.interkambio.SistemaInventarioBackend.model.PaymentReceived;
import org.interkambio.SistemaInventarioBackend.model.PaymentStatus;
import org.interkambio.SistemaInventarioBackend.model.SaleOrder;
import org.interkambio.SistemaInventarioBackend.model.SaleOrderStatus;
import org.interkambio.SistemaInventarioBackend.repository.PaymentReceivedRepository;
import org.interkambio.SistemaInventarioBackend.repository.SaleOrderRepository;
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
    private final SaleOrderRepository saleOrderRepository;

    @Override
    @Transactional
    public PaymentReceivedDTO save(PaymentReceivedDTO dto) {
        PaymentReceived entity = mapper.toEntity(dto);

        // Validar que el SaleOrder exista
        SaleOrder order = saleOrderRepository.findById(dto.getSaleOrderId())
                .orElseThrow(() -> new EntityNotFoundException("SaleOrder con id " + dto.getSaleOrderId() + " no encontrado"));

        entity.setSaleOrder(order);

        // Calcular total pagado actual
        BigDecimal totalPagadoActual = repository.findBySaleOrderId(order.getId()).stream()
                .map(PaymentReceived::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calcular saldo pendiente considerando amount + shipment + fee
        BigDecimal totalOrden = getOrderTotal(order);
        BigDecimal saldoPendiente = totalOrden.subtract(totalPagadoActual);

        if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El pago debe ser mayor a 0");
        }
        if (dto.getAmount().compareTo(saldoPendiente) > 0) {
            throw new IllegalArgumentException("El pago excede el saldo pendiente de la orden");
        }

        PaymentReceived saved = repository.save(entity);

        // Actualizar estado de la orden
        UpdateOrderStatus(order);

        return mapper.toDTO(saved);
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
    @Transactional
    public Optional<PaymentReceivedDTO> update(Long id, PaymentReceivedDTO dto) {
        return repository.findById(id).map(existing -> {
            SaleOrder order = existing.getSaleOrder();

            existing.setPaymentDate(dto.getPaymentDate());
            existing.setPaymentMethod(dto.getPaymentMethod());
            existing.setAmount(dto.getAmount());
            existing.setReferenceNumber(dto.getReferenceNumber());

            PaymentReceived updated = repository.save(existing);

            // Recalcular estado del pedido
            UpdateOrderStatus(order);

            return mapper.toDTO(updated);
        });
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return repository.findById(id).map(existing -> {
            SaleOrder order = existing.getSaleOrder();
            repository.delete(existing);

            // Recalcular estado del pedido después de eliminar pago
            UpdateOrderStatus(order);

            return true;
        }).orElse(false);
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

    private void UpdateOrderStatus(SaleOrder order) {
        BigDecimal totalPaid = repository.findBySaleOrderId(order.getId()).stream()
                .map(p -> p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalOrden = getOrderTotal(order);

        if (totalPaid.compareTo(BigDecimal.ZERO) == 0) {
            order.setPaymentStatus(PaymentStatus.UNPAID);
        } else if (totalPaid.compareTo(totalOrden) >= 0) {
            order.setPaymentStatus(PaymentStatus.PAID);
        } else {
            order.setPaymentStatus(PaymentStatus.PARTIALLY_PAID);
        }

        saleOrderRepository.save(order);
    }

    private BigDecimal getOrderTotal(SaleOrder order) {
        return (order.getAmount() != null ? order.getAmount() : BigDecimal.ZERO)
                .add(order.getAmountShipment() != null ? order.getAmountShipment() : BigDecimal.ZERO)
                .add(order.getAdditionalFee() != null ? order.getAdditionalFee() : BigDecimal.ZERO);
    }
}
