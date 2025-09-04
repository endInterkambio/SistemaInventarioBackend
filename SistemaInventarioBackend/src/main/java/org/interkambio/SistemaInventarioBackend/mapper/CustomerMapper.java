package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.sales.CustomerDTO;
import org.interkambio.SistemaInventarioBackend.model.Customer;
import org.interkambio.SistemaInventarioBackend.model.CustomerType;
import org.interkambio.SistemaInventarioBackend.model.DocumentType;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CustomerMapper implements GenericMapper<Customer, CustomerDTO> {

    private final CustomerContactMapper contactMapper;

    public CustomerMapper(CustomerContactMapper contactMapper) {
        this.contactMapper = contactMapper;
    }

    @Override
    public Customer toEntity(CustomerDTO dto) {
        if (dto == null) return null;

        Customer entity = new Customer();
        entity.setId(dto.getId());
        entity.setCustomerType(dto.getCustomerType() != null ?
                Enum.valueOf(CustomerType.class, dto.getCustomerType()) : null);
        entity.setDocumentType(dto.getDocumentType() != null ?
                Enum.valueOf(DocumentType.class, dto.getDocumentType()) : null);
        entity.setDocumentNumber(dto.getDocumentNumber());
        entity.setName(dto.getName());
        entity.setCompanyName(dto.getCompanyName());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setAddress(dto.getAddress());

        if (dto.getContacts() != null) {
            entity.setContacts(dto.getContacts().stream()
                    .map(contactDto -> {
                        var contact = contactMapper.toEntity(contactDto);
                        contact.setCustomer(entity); // establecer relación bidireccional
                        return contact;
                    })
                    .collect(Collectors.toList()));
        }

        return entity;
    }

    @Override
    public CustomerDTO toDTO(Customer entity) {
        if (entity == null) return null;

        CustomerDTO dto = new CustomerDTO();
        dto.setId(entity.getId());
        dto.setCustomerType(entity.getCustomerType() != null ? entity.getCustomerType().name() : null);
        dto.setDocumentType(entity.getDocumentType() != null ? entity.getDocumentType().name() : null);
        dto.setDocumentNumber(entity.getDocumentNumber());
        dto.setName(entity.getName());
        dto.setCompanyName(entity.getCompanyName());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setAddress(entity.getAddress());

        if (entity.getContacts() != null) {
            dto.setContacts(entity.getContacts().stream()
                    .map(contactMapper::toDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}

