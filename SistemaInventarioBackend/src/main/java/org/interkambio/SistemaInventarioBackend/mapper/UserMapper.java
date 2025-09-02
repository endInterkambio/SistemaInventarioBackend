package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.RoleDTO;
import org.interkambio.SistemaInventarioBackend.DTO.UserDTO;
import org.interkambio.SistemaInventarioBackend.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements GenericMapper<User, UserDTO> {

    @Override
    public UserDTO toDTO(User entity) {
        return new UserDTO(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getRole() != null
                        ? new RoleDTO(entity.getRole().getId(), entity.getRole().getName())
                        : null
        );
    }

    @Override
    public User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());

        if (dto.getRole() != null) {
            var role = new org.interkambio.SistemaInventarioBackend.model.Role();
            role.setId(dto.getRole().getId());
            role.setName(dto.getRole().getName());
            user.setRole(role);
        }

        return user;
    }
}
