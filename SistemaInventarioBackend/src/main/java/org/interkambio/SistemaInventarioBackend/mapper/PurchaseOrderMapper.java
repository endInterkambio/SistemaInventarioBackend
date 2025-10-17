package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.common.SimpleIdNameDTO;
import org.interkambio.SistemaInventarioBackend.DTO.purchase.PurchaseOrderDTO;
import org.interkambio.SistemaInventarioBackend.model.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PurchaseOrderMapper implements GenericMapper<PurchaseOrder, PurchaseOrderDTO> {

    private final PurchaseOrderItemMapper itemMapper;

    public PurchaseOrderMapper(PurchaseOrderItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    @Override
    public PurchaseOrder toEntity(PurchaseOrderDTO dto) {
        if (dto == null) return null;

        PurchaseOrder order = new PurchaseOrder();
        order.setId(dto.getId());
        order.setPurchaseOrderNumber(dto.getPurchaseOrderNumber());
        order.setPurchaseDate(dto.getPurchaseDate());
        order.setCreatedAt(dto.getCreatedAt());
        order.setPurchaseChannel(dto.getPurchaseChannel());
        order.setAmount(dto.getAmount());
        order.setAmountShipment(dto.getAmountShipment());
        order.setAdditionalFee(dto.getAdditionalFee());
        order.setStatus(dto.getStatus());
        order.setPaymentStatus(dto.getPaymentStatus() != null ? dto.getPaymentStatus() : PaymentStatus.UNPAID);
        order.setSupplierNotes(dto.getSupplierNotes());
        order.setDeliveryDate(dto.getDeliveryDate());

        // Mapeo de usuario creador
        if (dto.getCreatedBy() != null && dto.getCreatedBy().getId() != null) {
            User user = new User();
            user.setId(dto.getCreatedBy().getId());
            order.setCreatedBy(user);
        }

        // Mapeo de proveedor
        if (dto.getSupplier() != null && dto.getSupplier().getId() != null) {
            Supplier supplier = new Supplier();
            supplier.setId(dto.getSupplier().getId());
            order.setSupplier(supplier);
        } else {
            order.setSupplier(null);
        }

        // Mapeo de ítems
        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            order.setItems(dto.getItems().stream()
                    .map(itemDto -> {
                        var item = itemMapper.toEntity(itemDto);
                        item.setOrder(order); // mantener relación bidireccional
                        return item;
                    })
                    .collect(Collectors.toList()));
        }

        return order;
    }

    @Override
    public PurchaseOrderDTO toDTO(PurchaseOrder entity) {
        if (entity == null) return null;

        PurchaseOrderDTO dto = new PurchaseOrderDTO();
        dto.setId(entity.getId());
        dto.setPurchaseOrderNumber(entity.getPurchaseOrderNumber());
        dto.setPurchaseDate(entity.getPurchaseDate());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setPurchaseChannel(entity.getPurchaseChannel());
        dto.setAmount(entity.getAmount());
        dto.setAmountShipment(entity.getAmountShipment());
        dto.setAdditionalFee(entity.getAdditionalFee());
        dto.setStatus(entity.getStatus());
        dto.setPaymentStatus(entity.getPaymentStatus());
        dto.setSupplierNotes(entity.getSupplierNotes());
        dto.setDeliveryDate(entity.getDeliveryDate());

        // Mapeo de usuario creador
        if (entity.getCreatedBy() != null) {
            dto.setCreatedBy(new SimpleIdNameDTO(
                    entity.getCreatedBy().getId(),
                    entity.getCreatedBy().getUsername()
            ));
        }

        // Mapeo de proveedor
        if (entity.getSupplier() != null) {
            dto.setSupplier(new SimpleIdNameDTO(
                    entity.getSupplier().getId(),
                    entity.getSupplier().getName()
            ));
        } else {
            dto.setSupplier(null);
        }


        // Mapeo de ítems
        if (entity.getItems() != null && !entity.getItems().isEmpty()) {
            dto.setItems(entity.getItems().stream()
                    .map(itemMapper::toDTO)
                    .collect(Collectors.toList()));
        }

        // Totales calculados desde el modelo
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setTotalPaid(entity.getTotalPaid());

        return dto;
    }
}
