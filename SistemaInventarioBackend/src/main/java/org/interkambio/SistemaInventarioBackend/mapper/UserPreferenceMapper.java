package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.UserPreferenceDTO;
import org.interkambio.SistemaInventarioBackend.model.User;
import org.interkambio.SistemaInventarioBackend.model.UserPreference;

public class UserPreferenceMapper {

    public static UserPreferenceDTO toDTO(UserPreference userPreference) {
        return new UserPreferenceDTO(
                userPreference.getId(),
                userPreference.getPreferenceKey(),
                userPreference.getPreferenceValue(),
                userPreference.getUser() != null ? userPreference.getUser().getId() : null
        );
    }

    public static UserPreference toEntity(UserPreferenceDTO dto) {
        UserPreference userPreference = new UserPreference();
        userPreference.setId(dto.getId());
        userPreference.setPreferenceKey(dto.getPreferenceKey());
        userPreference.setPreferenceValue(dto.getPreferenceValue());
        if (dto.getUserId() != null) {
            User user = new User();
            user.setId(dto.getUserId());
            userPreference.setUser(user);
        }
        return userPreference;
    }
}
