package org.interkambio.SistemaInventarioBackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.sales.ShipmentDTO;
import org.interkambio.SistemaInventarioBackend.criteria.ShipmentSearchCriteria;
import org.interkambio.SistemaInventarioBackend.mapper.ShipmentMapper;
import org.interkambio.SistemaInventarioBackend.model.SaleOrder;
import org.interkambio.SistemaInventarioBackend.model.Shipment;
import org.interkambio.SistemaInventarioBackend.repository.SaleOrderRepository;
import org.interkambio.SistemaInventarioBackend.repository.ShipmentRepository;
import org.interkambio.SistemaInventarioBackend.service.GenericService;
import org.interkambio.SistemaInventarioBackend.service.ShipmentService;
import org.interkambio.SistemaInventarioBackend.specification.ShipmentSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
public class ShipmentServiceImpl implements ShipmentService, GenericService<ShipmentDTO, Long> {

    private final ShipmentRepository repository;
    private final ShipmentMapper mapper;
    private final SaleOrderRepository saleOrderRepository;

    @Override
    public Page<ShipmentDTO> findAllShipments(Pageable pageable) {
        Page<Long> idsPage = repository.findAllIds(pageable);
        List<Shipment> shipments = repository.findAllWithRelations(idsPage.getContent());
        List<ShipmentDTO> dtos = shipments.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, idsPage.getTotalElements());
    }

    @Override
    public Optional<ShipmentDTO> findByTrackingNumber(String trackingNumber) {
        return repository.findByTrackingNumber(trackingNumber).map(mapper::toDTO);
    }

    @Override
    public Optional<ShipmentDTO> findById(Long id) {
        return repository.findById(id).map(mapper::toDTO);
    }

    @Override
    public Optional<ShipmentDTO> partialUpdate(Long id, Map<String, Object> updates) {
        return repository.findById(id)
                .map(shipment -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "trackingNumber" -> shipment.setTrackingNumber((String) value);
                            case "address" -> shipment.setAddress((String) value);
                            case "shipmentDate" -> shipment.setShipmentDate(LocalDateTime.parse((String) value));
                            case "shippingFee" -> shipment.setShippingFee(parseBigDecimal(value));
                        }
                    });
                    return mapper.toDTO(repository.save(shipment));
                });
    }

    @Override
    public Page<ShipmentDTO> searchShipments(ShipmentSearchCriteria criteria, Pageable pageable) {
        Specification<Shipment> specification = ShipmentSpecification.withFilters(criteria);
        Page<Long> idsPage = repository.findAll(specification, pageable)
                .map(Shipment::getId);
        List<Shipment> shipments = repository.findAllWithRelations(idsPage.getContent());
        List<ShipmentDTO> dtos = shipments.stream().map(mapper::toDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, idsPage.getTotalElements());
    }

    @Override
    @Transactional
    public ShipmentDTO save(ShipmentDTO shipmentDTO) {
        Shipment shipment = mapper.toEntity(shipmentDTO);

        // Relación con Order
        if (shipment.getOrder() != null && shipment.getOrder().getId() != null) {
            SaleOrder order = saleOrderRepository.findById(shipment.getOrder().getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "SaleOrder con id " + shipment.getOrder().getId() + " no encontrado"
                    ));
            shipment.setOrder(order);
        }

        // Cascade: los items se asignan automáticamente al guardar Shipment
        if (shipment.getItems() != null) {
            shipment.getItems().forEach(item -> item.setShipment(shipment));
        }

        Shipment saved = repository.save(shipment);
        return mapper.toDTO(saved);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        Optional<Shipment> shipmentOpt = repository.findById(id);
        if (shipmentOpt.isPresent()) {
            repository.delete(shipmentOpt.get());
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

    // ===================== MÉTODOS GENÉRICOS NO IMPLEMENTADOS =====================

    @Override
    public List<ShipmentDTO> findAll() {
        throw new UnsupportedOperationException("findAll no implementado para Shipment");
    }

    @Override
    public List<ShipmentDTO> saveAll(List<ShipmentDTO> dtos) {
        throw new UnsupportedOperationException("saveAll no implementado para Shipment");
    }

    @Override
    public Optional<ShipmentDTO> update(Long id, ShipmentDTO dto) {
        throw new UnsupportedOperationException("update no implementado para Shipment");
    }
}
