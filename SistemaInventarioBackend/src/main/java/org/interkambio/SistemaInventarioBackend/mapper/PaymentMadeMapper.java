package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.purchase.PaymentMadeDTO;
import org.interkambio.SistemaInventarioBackend.model.PaymentMade;
import org.interkambio.SistemaInventarioBackend.model.PurchaseOrder;
import org.springframework.stereotype.Component;

@Component
public class PaymentMadeMapper implements GenericMapper<PaymentMade, PaymentMadeDTO> {

    @Override
    public PaymentMade toEntity(PaymentMadeDTO dto) {
        if (dto == null) {
            return null;
        }

        PaymentMade entity = new PaymentMade();
        entity.setId(dto.getId());
        entity.setPaymentDate(dto.getPaymentDate());
        entity.setAmount(dto.getAmount());
        entity.setReferenceNumber(dto.getReferenceNumber());

        // Relación con PurchaseOrder: solo asignamos el ID
        if (dto.getPurchaseOrderId() != null) {
            PurchaseOrder order = new PurchaseOrder();
            order.setId(dto.getPurchaseOrderId());
            entity.setPurchaseOrder(order);
        }

        return entity;
    }

    @Override
    public PaymentMadeDTO toDTO(PaymentMade entity) {
        if (entity == null) {
            return null;
        }

        PaymentMadeDTO dto = new PaymentMadeDTO();
        dto.setId(entity.getId());
        dto.setPaymentDate(entity.getPaymentDate());
        dto.setAmount(entity.getAmount());
        dto.setReferenceNumber(entity.getReferenceNumber());

        if (entity.getPurchaseOrder() != null) {
            dto.setPurchaseOrderId(entity.getPurchaseOrder().getId());
        }

        return dto;
    }
}
