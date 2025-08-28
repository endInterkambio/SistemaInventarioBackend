package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.CustomerContactDTO;
import org.interkambio.SistemaInventarioBackend.model.CustomerContact;
import org.springframework.stereotype.Component;

@Component
public class CustomerContactMapper implements GenericMapper<CustomerContact, CustomerContactDTO> {

    @Override
    public CustomerContact toEntity(CustomerContactDTO dto) {
        if (dto == null) return null;

        CustomerContact entity = new CustomerContact();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());

        // Nota: la relación Customer se debe asignar desde el servicio, no aquí
        // entity.setCustomer(...);

        return entity;
    }

    @Override
    public CustomerContactDTO toDTO(CustomerContact entity) {
        if (entity == null) return null;

        CustomerContactDTO dto = new CustomerContactDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());

        if (entity.getCustomer() != null) {
            dto.setCustomerId(entity.getCustomer().getId());
        }

        return dto;
    }
}

