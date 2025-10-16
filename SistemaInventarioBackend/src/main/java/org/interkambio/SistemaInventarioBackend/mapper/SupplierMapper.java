package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.purchase.SupplierDTO;
import org.interkambio.SistemaInventarioBackend.model.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper implements GenericMapper<Supplier, SupplierDTO> {

    @Override
    public Supplier toEntity(SupplierDTO dto) {
        if (dto == null) return null;

        Supplier supplier = new Supplier();
        supplier.setId(dto.getId());
        supplier.setName(dto.getName());
        supplier.setContactPerson(dto.getContactPerson());
        supplier.setEmail(dto.getEmail());
        supplier.setPhoneNumber(dto.getPhoneNumber());
        supplier.setAddress(dto.getAddress());

        return supplier;
    }

    @Override
    public SupplierDTO toDTO(Supplier entity) {
        if (entity == null) return null;

        SupplierDTO dto = new SupplierDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setContactPerson(entity.getContactPerson());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setAddress(entity.getAddress());

        return dto;
    }
}
