package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.user.RoleDTO;
import org.interkambio.SistemaInventarioBackend.model.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper implements GenericMapper<Role, RoleDTO> {

    @Override
    public Role toEntity(RoleDTO dto) {
        Role role = new Role();
        role.setId(dto.getId());
        role.setName(dto.getName());
        return role;
    }

    @Override
    public RoleDTO toDTO(Role entity) {
        return new RoleDTO(entity.getId(), entity.getName());
    }
}
