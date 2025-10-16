package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.sales.SaleOrderCustomerDTO;
import org.interkambio.SistemaInventarioBackend.DTO.sales.SaleOrderDTO;
import org.interkambio.SistemaInventarioBackend.DTO.common.SimpleIdNameDTO;
import org.interkambio.SistemaInventarioBackend.DTO.sales.ShipmentDTO;
import org.interkambio.SistemaInventarioBackend.model.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SaleOrderMapper implements GenericMapper<SaleOrder, SaleOrderDTO> {

    private final SaleOrderItemMapper itemMapper;

    public SaleOrderMapper(SaleOrderItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    @Override
    public SaleOrder toEntity(SaleOrderDTO dto) {
        SaleOrder order = new SaleOrder();
        order.setId(dto.getId());
        order.setOrderNumber(dto.getOrderNumber());
        order.setOrderDate(dto.getOrderDate());
        order.setCreatedAt(dto.getCreatedAt());
        order.setSaleChannel(dto.getSaleChannel());
        order.setAmount(dto.getAmount());
        order.setAmountShipment(dto.getAmountShipment());
        order.setAdditionalFee(dto.getAdditionalFee());
        order.setStatus(dto.getStatus());
        if (dto.getPaymentStatus() != null) {
            order.setPaymentStatus(dto.getPaymentStatus());
        } else {
            order.setPaymentStatus(PaymentStatus.UNPAID);
        }
        order.setCustomerNotes(dto.getCustomerNotes());

        // Mapeo del usuario que creó la orden
        if (dto.getCreatedBy() != null && dto.getCreatedBy().getId() != null) {
            var user = new User();
            user.setId(dto.getCreatedBy().getId());
            order.setCreatedBy(user);
        }

        // Mapeo del customer usando SaleOrderCustomerDTO
        if (dto.getCustomer() != null && dto.getCustomer().getId() != null) {
            var customer = new Customer();
            customer.setId(dto.getCustomer().getId());
            // Mapear customerType de String a enum
            if (dto.getCustomer().getCustomerType() != null) {
                customer.setCustomerType(CustomerType.valueOf(dto.getCustomer().getCustomerType()));
            }
            order.setCustomer(customer);
        }

        // Mapeo de items
        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            order.setItems(dto.getItems().stream()
                    .map(itemDto -> {
                        var item = itemMapper.toEntity(itemDto);
                        item.setOrder(order); // mantener relación bidireccional
                        return item;
                    })
                    .collect(Collectors.toList()));
        }

        // Mapeo de shipment
        if (dto.getShipment() != null && dto.getShipment().getId() != null) {
            Shipment shipment = new Shipment();
            shipment.setId(dto.getShipment().getId());
            shipment.setShipmentDate(dto.getShipment().getShipmentDate());
            shipment.setTrackingNumber(dto.getShipment().getTrackingNumber());
            shipment.setAddress(dto.getShipment().getAddress());
            shipment.setShippingFee(dto.getShipment().getShippingFee());

            if (dto.getShipment().getShipmentMethod() != null) {
                ShipmentMethod method = new ShipmentMethod();
                method.setId(dto.getShipment().getShipmentMethod().getId());
                shipment.setShipmentMethod(method);
            }

            order.setShipment(shipment);
        }

        return order;
    }

    @Override
    public SaleOrderDTO toDTO(SaleOrder entity) {
        SaleOrderDTO dto = new SaleOrderDTO();
        dto.setId(entity.getId());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setOrderDate(entity.getOrderDate());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setSaleChannel(entity.getSaleChannel());
        dto.setAmount(entity.getAmount());
        dto.setAmountShipment(entity.getAmountShipment());
        dto.setAdditionalFee(entity.getAdditionalFee());
        dto.setStatus(entity.getStatus());
        dto.setPaymentStatus(entity.getPaymentStatus());
        dto.setTotalPaid(entity.getTotalPaid());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setCustomerNotes(entity.getCustomerNotes());

        // Mapeo del usuario que creó la orden
        if (entity.getCreatedBy() != null) {
            dto.setCreatedBy(new SimpleIdNameDTO(
                    entity.getCreatedBy().getId(),
                    entity.getCreatedBy().getUsername()
            ));
        }

        // Mapeo del customer usando SaleOrderCustomerDTO
        dto.setCustomer(new SaleOrderCustomerDTO(
                entity.getCustomer().getId(),
                entity.getCustomer().getCustomerType() == CustomerType.PERSON ? entity.getCustomer().getName() : null,
                entity.getCustomer().getCustomerType() == CustomerType.COMPANY ? entity.getCustomer().getCompanyName() : null,
                entity.getCustomer().getCustomerType() != null ? entity.getCustomer().getCustomerType().name() : null
        ));

        // Mapeo de items
        if (entity.getItems() != null && !entity.getItems().isEmpty()) {
            dto.setItems(entity.getItems().stream()
                    .map(itemMapper::toDTO)
                    .collect(Collectors.toList()));
        }

        // Mapeo del shipment (único por orden, opcional)
        if (entity.getShipment() != null) {
            Shipment shipment = entity.getShipment();
            ShipmentDTO shipmentDTO = new ShipmentDTO();
            shipmentDTO.setId(shipment.getId());
            shipmentDTO.setShipmentDate(shipment.getShipmentDate());
            shipmentDTO.setTrackingNumber(shipment.getTrackingNumber());
            shipmentDTO.setAddress(shipment.getAddress());
            shipmentDTO.setShippingFee(shipment.getShippingFee());

            if (shipment.getShipmentMethod() != null) {
                shipmentDTO.setShipmentMethod(new SimpleIdNameDTO(
                        shipment.getShipmentMethod().getId(),
                        shipment.getShipmentMethod().getName()
                ));
            }

            // 👇 No mapeamos items, quedará null (para evitar redundancia)
            dto.setShipment(shipmentDTO);
        }


        return dto;
    }

}
