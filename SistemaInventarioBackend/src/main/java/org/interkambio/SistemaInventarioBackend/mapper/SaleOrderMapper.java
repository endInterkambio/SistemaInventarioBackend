package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.SaleOrderDTO;
import org.interkambio.SistemaInventarioBackend.DTO.SimpleIdNameDTO;
import org.interkambio.SistemaInventarioBackend.model.CustomerType;
import org.interkambio.SistemaInventarioBackend.model.SaleOrder;
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

        if (dto.getCreatedBy() != null && dto.getCreatedBy().getId() != null) {
            var user = new org.interkambio.SistemaInventarioBackend.model.User();
            user.setId(dto.getCreatedBy().getId());
            order.setCreatedBy(user);
        }

        if (dto.getCustomer() != null && dto.getCustomer().getId() != null) {
            var customer = new org.interkambio.SistemaInventarioBackend.model.Customer();
            customer.setId(dto.getCustomer().getId());
            order.setCustomer(customer);
        }

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

        if (entity.getCreatedBy() != null) {
            dto.setCreatedBy(new SimpleIdNameDTO(
                    entity.getCreatedBy().getId(),
                    entity.getCreatedBy().getUsername()
            ));
        }

        if (entity.getCustomer() != null) {
            String customerName;
            if (entity.getCustomer().getCustomerType() == CustomerType.PERSON) {
                customerName = entity.getCustomer().getName();
            } else {
                customerName = entity.getCustomer().getCompanyName();
            }

            dto.setCustomer(new SimpleIdNameDTO(
                    entity.getCustomer().getId(),
                    customerName
            ));
        }


        if (entity.getItems() != null && !entity.getItems().isEmpty()) {
            dto.setItems(entity.getItems().stream()
                    .map(itemMapper::toDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
