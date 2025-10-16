package org.interkambio.SistemaInventarioBackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.purchase.PurchaseOrderDTO;
import org.interkambio.SistemaInventarioBackend.criteria.PurchaseOrderSearchCriteria;
import org.interkambio.SistemaInventarioBackend.mapper.PurchaseOrderMapper;
import org.interkambio.SistemaInventarioBackend.model.*;
import org.interkambio.SistemaInventarioBackend.repository.*;
import org.interkambio.SistemaInventarioBackend.service.PurchaseOrderService;
import org.interkambio.SistemaInventarioBackend.service.GenericService;
import org.interkambio.SistemaInventarioBackend.specification.PurchaseOrderSearchSpecification;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService, GenericService<PurchaseOrderDTO, Long> {

    private final PurchaseOrderRepository repository;
    private final PurchaseOrderMapper mapper;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final BookStockLocationRepository bookStockLocationRepository;

    @Override
    public Page<PurchaseOrderDTO> findAllOrders(Pageable pageable) {
        Page<Long> idsPage = repository.findAllIds(pageable);
        List<PurchaseOrder> orders = repository.findAllWithRelations(idsPage.getContent());
        List<PurchaseOrderDTO> dtos = orders.stream().map(mapper::toDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, idsPage.getTotalElements());
    }

    @Override
    public Optional<PurchaseOrderDTO> findByPurchaseOrderNumber(String orderNumber) {
        return repository.findByPurchaseOrderNumber(orderNumber).map(mapper::toDTO);
    }

    @Override
    public Optional<PurchaseOrderDTO> findById(Long id) {
        return repository.findById(id).map(mapper::toDTO);
    }

    @Override
    public Optional<PurchaseOrderDTO> partialUpdate(Long id, Map<String, Object> updates) {
        return repository.findById(id)
                .map(order -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "amount" -> order.setAmount(parseBigDecimal(value));
                            case "amountShipment" -> order.setAmountShipment(parseBigDecimal(value));
                            case "additionalFee" -> order.setAdditionalFee(parseBigDecimal(value));
                            case "status" -> order.setStatus(OrderStatus.valueOf(value.toString()));
                        }
                    });
                    return mapper.toDTO(repository.save(order));
                });
    }

    @Override
    public Page<PurchaseOrderDTO> searchPurchaseOrders(PurchaseOrderSearchCriteria criteria, Pageable pageable) {
        Specification<PurchaseOrder> specification = PurchaseOrderSearchSpecification.withFilters(criteria);

        Pageable sortedPageable = pageable;
        if (criteria.getSortBy() != null && !criteria.getSortBy().isEmpty()) {
            Sort.Direction direction = "desc".equalsIgnoreCase(criteria.getSortDirection())
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;

            sortedPageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(direction, criteria.getSortBy())
            );
        }

        Page<Long> idsPage = repository.findAll(specification, sortedPageable).map(PurchaseOrder::getId);
        List<PurchaseOrder> orders = repository.findAllWithRelations(idsPage.getContent());
        List<PurchaseOrderDTO> dtos = orders.stream().map(mapper::toDTO).collect(Collectors.toList());

        return new PageImpl<>(dtos, sortedPageable, idsPage.getTotalElements());
    }

    @Override
    public String getNextOrderNumber() {
        String lastOrder = repository.findLastOrderNumber(PageRequest.of(0, 1))
                .stream().findFirst().orElse(null);
        return generateOrderNumber(lastOrder);
    }

    @Override
    @Transactional
    public PurchaseOrderDTO save(PurchaseOrderDTO dto) {
        PurchaseOrder order = mapper.toEntity(dto);

        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.PENDING);
        }

        if (order.getCreatedAt() == null) {
            order.setCreatedAt(LocalDateTime.now());
        }

        if (order.getPurchaseOrderNumber() == null || order.getPurchaseOrderNumber().isBlank()) {
            String lastOrder = repository.findLastOrderNumber(PageRequest.of(0,1))
                    .stream().findFirst().orElse(null);
            order.setPurchaseOrderNumber(generateOrderNumber(lastOrder));
        }

        if (order.getSupplier() != null && order.getSupplier().getId() != null) {
            Supplier supplier = supplierRepository.findById(order.getSupplier().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Supplier con id " + order.getSupplier().getId() + " no encontrado"));
            order.setSupplier(supplier);
        }

        if (order.getItems() != null && !order.getItems().isEmpty()) {
            List<Long> locationIds = order.getItems().stream()
                    .map(item -> item.getBookStockLocation().getId())
                    .collect(Collectors.toList());

            List<BookStockLocation> locations = bookStockLocationRepository.findAllById(locationIds);
            Map<Long, BookStockLocation> locationMap = locations.stream()
                    .collect(Collectors.toMap(BookStockLocation::getId, loc -> loc));

            for (var item : order.getItems()) {
                Long locId = item.getBookStockLocation().getId();
                BookStockLocation location = locationMap.get(locId);
                if (location == null) {
                    throw new EntityNotFoundException("BookStockLocation con id " + locId + " no encontrado");
                }
                if (location.getBook() == null) {
                    throw new IllegalStateException("BookStockLocation con id " + locId + " no tiene Book asignado");
                }
                item.setBookStockLocation(location);
                item.setOrder(order);
            }
        }

        PurchaseOrder saved = repository.save(order);
        return mapper.toDTO(saved);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        Optional<PurchaseOrder> orderOpt = repository.findById(id);
        if (orderOpt.isPresent()) {
            repository.delete(orderOpt.get());
            return true;
        } else {
            return false;
        }
    }

    private BigDecimal parseBigDecimal(Object value) {
        if (value == null) return null;
        if (value instanceof Number n) return BigDecimal.valueOf(n.doubleValue());
        if (value instanceof String s) return new BigDecimal(s);
        throw new IllegalArgumentException("No se puede convertir a BigDecimal: " + value);
    }

    private String generateOrderNumber(String lastOrderNumber) {
        int next = 1;
        if (lastOrderNumber != null && lastOrderNumber.startsWith("PO-")) {
            try {
                next = Integer.parseInt(lastOrderNumber.substring(3)) + 1;
            } catch (NumberFormatException ignored) {}
        }
        return String.format("PO-%05d", next);
    }

    // ===================== MÉTODOS GENÉRICOS NO IMPLEMENTADOS =====================

    @Override
    public List<PurchaseOrderDTO> findAll() {
        throw new UnsupportedOperationException("findAll no implementado para PurchaseOrder");
    }

    @Override
    public List<PurchaseOrderDTO> saveAll(List<PurchaseOrderDTO> dtos) {
        throw new UnsupportedOperationException("saveAll no implementado para PurchaseOrder");
    }

    @Override
    public Optional<PurchaseOrderDTO> update(Long id, PurchaseOrderDTO dto) {
        throw new UnsupportedOperationException("update no implementado para PurchaseOrder");
    }
}
