package org.interkambio.SistemaInventarioBackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.sales.ShipmentDTO;
import org.interkambio.SistemaInventarioBackend.criteria.ShipmentSearchCriteria;
import org.interkambio.SistemaInventarioBackend.mapper.ShipmentMapper;
import org.interkambio.SistemaInventarioBackend.model.SaleOrder;
import org.interkambio.SistemaInventarioBackend.model.SaleOrderStatus;
import org.interkambio.SistemaInventarioBackend.model.Shipment;
import org.interkambio.SistemaInventarioBackend.model.ShipmentMethod;
import org.interkambio.SistemaInventarioBackend.repository.SaleOrderRepository;
import org.interkambio.SistemaInventarioBackend.repository.ShipmentMethodRepository;
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
public class ShipmentServiceImpl implements ShipmentService, GenericService<ShipmentDTO, Long> {

    private final ShipmentRepository repository;
    private final ShipmentMapper mapper;
    private final SaleOrderRepository saleOrderRepository;
    private final ShipmentMethodRepository shipmentMethodRepository;

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
    @Transactional
    public Optional<ShipmentDTO> partialUpdate(Long id, Map<String, Object> updates) {
        return repository.findById(id)
                .map(shipment -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "trackingNumber" -> shipment.setTrackingNumber((String) value);
                            case "address" -> shipment.setAddress((String) value);
                            case "shipmentDate" -> shipment.setShipmentDate(LocalDateTime.parse((String) value));
                            case "shippingFee" -> shipment.setShippingFee(parseBigDecimal(value));
                            case "shipmentMethodId" -> {  // NUEVO
                                Long methodId = parseLong(value);
                                ShipmentMethod method = shipmentMethodRepository.findById(methodId)
                                        .orElseThrow(() -> new EntityNotFoundException(
                                                "ShipmentMethod con id " + methodId + " no encontrado"
                                        ));
                                shipment.setShipmentMethod(method);
                            }
                        }
                    });
                    return mapper.toDTO(repository.save(shipment));
                });
    }

    private Long parseLong(Object value) {
        if (value instanceof Number n) return n.longValue();
        if (value instanceof String s) return Long.parseLong(s);
        throw new IllegalArgumentException("No se puede convertir a Long: " + value);
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
        // Convertir DTO a entity (sin items)
        Shipment shipment = mapper.toEntity(shipmentDTO);

        SaleOrder order = null;
        if (shipmentDTO.getOrder().getId() != null) {
            order = saleOrderRepository.findById(shipmentDTO.getOrder().getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "SaleOrder con id " + shipmentDTO.getOrder().getId() + " no encontrado"
                    ));
            shipment.setOrder(order);

            // Cambiar estado de la orden automáticamente
            if (order.getStatus() == null || order.getStatus() == SaleOrderStatus.PENDING) {
                order.setStatus(SaleOrderStatus.IN_PROGRESS);
                saleOrderRepository.save(order);
            }
        }


        // Asociar ShipmentMethod existente
        if (shipmentDTO.getShipmentMethod() != null && shipmentDTO.getShipmentMethod().getId() != null) {
            ShipmentMethod method = shipmentMethodRepository.findById(shipmentDTO.getShipmentMethod().getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "ShipmentMethod con id " + shipmentDTO.getShipmentMethod().getId() + " no encontrado"
                    ));
            shipment.setShipmentMethod(method);
        }

        // Guardar envío
        Shipment saved = repository.save(shipment);

        // Mapear a DTO (los items se toman de la orden)
        return mapper.toDTO(saved);
    }


    @Transactional
    @Override
    public boolean delete(Long id) {
        return repository.findById(id).map(shipment -> {
            // Recuperamos la orden asociada
            SaleOrder order = shipment.getOrder();
            if (order != null) {
                // Rompemos relación
                order.setShipment(null);
                // Devolvemos estado original
                order.setStatus(SaleOrderStatus.PENDING); // Regresar al estado pendiente
            }

            // Borramos el envío
            repository.delete(shipment);
            repository.flush();

            return true;
        }).orElse(false);
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
