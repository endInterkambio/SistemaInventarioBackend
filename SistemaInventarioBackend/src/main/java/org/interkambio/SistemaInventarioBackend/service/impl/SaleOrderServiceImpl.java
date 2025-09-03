package org.interkambio.SistemaInventarioBackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.SaleOrderDTO;
import org.interkambio.SistemaInventarioBackend.criteria.SaleOrderSearchCriteria;
import org.interkambio.SistemaInventarioBackend.mapper.SaleOrderMapper;
import org.interkambio.SistemaInventarioBackend.model.*;
import org.interkambio.SistemaInventarioBackend.repository.*;
import org.interkambio.SistemaInventarioBackend.service.SaleOrderService;
import org.interkambio.SistemaInventarioBackend.service.GenericService;
import org.interkambio.SistemaInventarioBackend.specification.SaleOrderSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SaleOrderServiceImpl implements SaleOrderService, GenericService<SaleOrderDTO, Long> {

    private final SaleOrderRepository repository;
    private final SaleOrderMapper mapper;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final BookStockLocationRepository bookStockLocationRepository;
    private final BookRepository bookRepository;

    @Override
    public Page<SaleOrderDTO> findAllOrders(Pageable pageable) {
        Page<Long> idsPage = repository.findAllIds(pageable);
        List<SaleOrder> orders = repository.findAllWithRelations(idsPage.getContent());
        List<SaleOrderDTO> dtos = orders.stream().map(mapper::toDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, idsPage.getTotalElements());
    }

    @Override
    public Optional<SaleOrderDTO> findByOrderNumber(String orderNumber) {
        return repository.findByOrderNumber(orderNumber).map(mapper::toDTO);
    }

    @Override
    public Optional<SaleOrderDTO> findById(Long id) {
        return repository.findById(id).map(mapper::toDTO);
    }

    @Override
    public Optional<SaleOrderDTO> partialUpdate(Long id, Map<String, Object> updates) {
        return repository.findById(id)
                .map(order -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "saleChannel" -> order.setSaleChannel((String) value);
                            case "amount" ->
                                    order.setAmount((value instanceof Number n) ? BigDecimal.valueOf(n.doubleValue()) : null);
                            case "amountShipment" ->
                                    order.setAmountShipment((value instanceof Number n) ? BigDecimal.valueOf(n.doubleValue()) : null);
                            case "additionalFee" ->
                                    order.setAdditionalFee((value instanceof Number n) ? BigDecimal.valueOf(n.doubleValue()) : null);
                        }
                    });
                    return mapper.toDTO(repository.save(order));
                });
    }

    @Override
    public Page<SaleOrderDTO> searchOrders(SaleOrderSearchCriteria criteria, Pageable pageable) {
        Specification<SaleOrder> specification = SaleOrderSpecification.withFilters(criteria);
        Page<Long> idsPage = repository.findAll(specification, pageable)
                .map(SaleOrder::getId);
        List<SaleOrder> orders = repository.findAllWithRelations(idsPage.getContent());
        List<SaleOrderDTO> dtos = orders.stream().map(mapper::toDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, idsPage.getTotalElements());
    }

    @Override
    @Transactional
    public SaleOrderDTO save(SaleOrderDTO orderDTO) {
        SaleOrder order = mapper.toEntity(orderDTO);

        if (order.getCreatedAt() == null) {
            order.setCreatedAt(OffsetDateTime.now(ZoneOffset.systemDefault()));
        }

        if (order.getCustomer() != null && order.getCustomer().getId() != null) {
            order.setCustomer(
                    customerRepository.findById(order.getCustomer().getId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Customer con id " + order.getCustomer().getId() + " no encontrado"
                            ))
            );
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
                    throw new RuntimeException("BookStockLocation con id " + locId + " no encontrado");
                }

                if (location.getBook() == null) {
                    throw new RuntimeException("BookStockLocation con id " + locId + " no tiene Book asignado");
                }

                item.setBookStockLocation(location);
                item.setOrder(order);
            }
        }

        SaleOrder saved = repository.save(order);
        return mapper.toDTO(saved);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        Optional<SaleOrder> orderOpt = repository.findById(id);
        if (orderOpt.isPresent()) {
            repository.delete(orderOpt.get()); // CascadeType.ALL eliminará items
            return true;
        } else {
            return false;
        }
    }

    // ===================== MÉTODOS GENÉRICOS NO IMPLEMENTADOS =====================

    @Override
    public List<SaleOrderDTO> findAll() {
        throw new UnsupportedOperationException("findAll no implementado para SaleOrder");
    }

    @Override
    public List<SaleOrderDTO> saveAll(List<SaleOrderDTO> dtos) {
        throw new UnsupportedOperationException("saveAll no implementado para SaleOrder");
    }

    @Override
    public Optional<SaleOrderDTO> update(Long id, SaleOrderDTO dto) {
        throw new UnsupportedOperationException("update no implementado para SaleOrder");
    }
}
