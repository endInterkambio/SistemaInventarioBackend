package org.interkambio.SistemaInventarioBackend.mapper;

import org.interkambio.SistemaInventarioBackend.DTO.UserPreferenceDTO;
import org.interkambio.SistemaInventarioBackend.model.User;
import org.interkambio.SistemaInventarioBackend.model.UserPreference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserPreferenceMapper implements GenericMapper<UserPreference, UserPreferenceDTO> {

    @Autowired
    private org.interkambio.SistemaInventarioBackend.repository.UserRepository userRepository;

    @Override
    public UserPreference toEntity(UserPreferenceDTO dto) {
        UserPreference entity = new UserPreference();
        entity.setId(dto.getId());
        entity.setPreferenceKey(dto.getPreferenceKey());
        entity.setPreferenceValue(dto.getPreferenceValue());

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId()).orElse(null);
            entity.setUser(user);
        }

        return entity;
    }

    @Override
    public UserPreferenceDTO toDTO(UserPreference entity) {
        UserPreferenceDTO dto = new UserPreferenceDTO();
        dto.setId(entity.getId());
        dto.setPreferenceKey(entity.getPreferenceKey());
        dto.setPreferenceValue(entity.getPreferenceValue());

        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }

        return dto;
    }
}
