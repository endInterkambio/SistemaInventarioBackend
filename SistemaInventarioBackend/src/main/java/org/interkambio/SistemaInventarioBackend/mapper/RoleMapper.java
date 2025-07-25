package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.RoleDTO;
import org.interkambio.SistemaInventarioBackend.model.Role;

public class RoleMapper {
    public static RoleDTO toDTO(Role role) {
        return new RoleDTO(
                role.getId(),
                role.getName()
        );
    }

    public static Role toEntity(RoleDTO dto) {
        Role role = new Role();
        role.setId(dto.getId());
        role.setName(dto.getName());

        return role;
    }
}
