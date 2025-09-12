package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.sales.CustomerDTO;
import org.interkambio.SistemaInventarioBackend.DTO.sales.PaymentReceivedDTO;
import org.interkambio.SistemaInventarioBackend.DTO.sales.SaleOrderCustomerDTO;
import org.interkambio.SistemaInventarioBackend.model.PaymentReceived;
import org.interkambio.SistemaInventarioBackend.model.SaleOrder;
import org.springframework.stereotype.Component;

@Component
public class PaymentReceivedMapper implements GenericMapper<PaymentReceived, PaymentReceivedDTO> {

    @Override
    public PaymentReceived toEntity(PaymentReceivedDTO dto) {
        if (dto == null) {
            return null;
        }

        PaymentReceived entity = new PaymentReceived();
        entity.setId(dto.getId());
        entity.setPaymentDate(dto.getPaymentDate());
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setAmount(dto.getAmount());
        entity.setReferenceNumber(dto.getReferenceNumber());

        // Relación con SaleOrder: solo asignamos el id
        if (dto.getSaleOrderId() != null) {
            SaleOrder order = new SaleOrder();
            order.setId(dto.getSaleOrderId());
            entity.setSaleOrder(order);
        }

        return entity;
    }

    @Override
    public PaymentReceivedDTO toDTO(PaymentReceived entity) {
        if (entity == null) {
            return null;
        }

        PaymentReceivedDTO dto = new PaymentReceivedDTO();
        dto.setId(entity.getId());

        if (entity.getSaleOrder() != null) {
            SaleOrder order = entity.getSaleOrder();
            dto.setSaleOrderId(order.getId());
            dto.setSaleOrderNumber(order.getOrderNumber());

            // Mapear el customer usando SaleOrderCustomerDTO
            if (order.getCustomer() != null) {
                var customer = order.getCustomer();
                var customerDTO = new SaleOrderCustomerDTO();
                customerDTO.setId(customer.getId());
                customerDTO.setCustomerType(customer.getCustomerType().name());
                customerDTO.setName(customer.getName());
                customerDTO.setCompanyName(customer.getCompanyName());
                dto.setCustomer(customerDTO);
            }
        }

        dto.setPaymentDate(entity.getPaymentDate());
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setAmount(entity.getAmount());
        dto.setReferenceNumber(entity.getReferenceNumber());

        return dto;
    }
}
