package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.common.SimpleIdNameDTO;
import org.interkambio.SistemaInventarioBackend.DTO.purchase.PaymentMadeDTO;
import org.interkambio.SistemaInventarioBackend.model.PaymentMade;
import org.interkambio.SistemaInventarioBackend.model.PurchaseOrder;
import org.springframework.stereotype.Component;

@Component
public class PaymentMadeMapper implements GenericMapper<PaymentMade, PaymentMadeDTO> {

    @Override
    public PaymentMade toEntity(PaymentMadeDTO dto) {
        if (dto == null) return null;

        PaymentMade entity = new PaymentMade();
        entity.setId(dto.getId());
        entity.setPaymentDate(dto.getPaymentDate());
        entity.setAmount(dto.getAmount());
        entity.setReferenceNumber(dto.getReferenceNumber());
        entity.setPaymentMethod(dto.getPaymentMethod());

        // No asignamos purchaseOrder aquí, lo hará el service
        return entity;
    }

    @Override
    public PaymentMadeDTO toDTO(PaymentMade entity) {
        if (entity == null) return null;

        PaymentMadeDTO dto = new PaymentMadeDTO();
        dto.setId(entity.getId());
        dto.setPaymentDate(entity.getPaymentDate());
        dto.setAmount(entity.getAmount());
        dto.setReferenceNumber(entity.getReferenceNumber());
        dto.setPaymentMethod(entity.getPaymentMethod());

        if (entity.getPurchaseOrder() != null) {
            PurchaseOrder po = entity.getPurchaseOrder();
            dto.setPurchaseOrderId(po.getId());
            dto.setPurchaseOrderNumber(po.getPurchaseOrderNumber());

            // Obtener proveedor para referencia
            if (po.getSupplier() != null) {
                dto.setSupplier(new SimpleIdNameDTO(
                        po.getSupplier().getId(),
                        po.getSupplier().getName()
                ));
            }
        }

        return dto;
    }
}

