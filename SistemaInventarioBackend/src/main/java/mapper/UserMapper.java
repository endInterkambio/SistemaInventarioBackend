package mapper;

import DTO.UserDTO;
import model.Role;
import model.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole() != null ? user.getRole().getId() : null
        );
    }

    public static User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        if (dto.getRoleId() != null) {
            Role role = new Role();
            role.setId(dto.getRoleId());
            user.setRole(role);
        }
        return user;
    }
}
