package service;

import DTO.UserPreferenceDTO;

import java.util.List;
import java.util.Optional;

public interface UserPreferenceService {
    UserPreferenceDTO save(UserPreferenceDTO userPreferenceDTO);
    List<UserPreferenceDTO> listAll();
    Optional<UserPreferenceDTO> searchById(Long id);
    Optional<UserPreferenceDTO> update(Long id, UserPreferenceDTO dto);
    void delete(Long id);
}
