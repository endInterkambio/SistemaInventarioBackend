package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.sales.PaymentReceivedDTO;
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
        dto.setPaymentDate(entity.getPaymentDate());
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setAmount(entity.getAmount());
        dto.setReferenceNumber(entity.getReferenceNumber());

        // Extraer solo el ID del SaleOrder
        if (entity.getSaleOrder() != null) {
            dto.setSaleOrderId(entity.getSaleOrder().getId());
        }

        return dto;
    }
}
