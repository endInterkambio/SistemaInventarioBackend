package service.impl;

import DTO.UserPreferenceDTO;
import mapper.UserPreferenceMapper;
import model.UserPreference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.UserPreferenceRepository;
import service.UserPreferenceService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserPreferenceImpl implements UserPreferenceService {
    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    @Override
    public UserPreferenceDTO save(UserPreferenceDTO userPreferenceDTO) {
        UserPreference userPreference = UserPreferenceMapper.toEntity(userPreferenceDTO);
        UserPreference saved = userPreferenceRepository.save(userPreference);
        return UserPreferenceMapper.toDTO(saved);
    }

    @Override
    public List<UserPreferenceDTO> listAll() {
        return userPreferenceRepository.findAll()
                .stream()
                .map(UserPreferenceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserPreferenceDTO> searchById(Long id) {
        return userPreferenceRepository.findById(id)
                .map(UserPreferenceMapper::toDTO);
    }

    @Override
    public Optional<UserPreferenceDTO> update(Long id, UserPreferenceDTO userPreferenceDTO) {
        return userPreferenceRepository.findById(id).map(existente -> {
            UserPreference userPreference = UserPreferenceMapper.toEntity(userPreferenceDTO);
            userPreference.setId(id); // aseguramos que se actualice el existente
            UserPreference updated = userPreferenceRepository.save(userPreference);
            return UserPreferenceMapper.toDTO(updated);
        });
    }

    @Override
    public void delete(Long id) {
        userPreferenceRepository.deleteById(id);
    }
}
