package org.interkambio.SistemaInventarioBackend.service.impl;

import org.interkambio.SistemaInventarioBackend.DTO.UserPreferenceDTO;
import org.interkambio.SistemaInventarioBackend.mapper.UserPreferenceMapper;
import org.interkambio.SistemaInventarioBackend.model.UserPreference;
import org.interkambio.SistemaInventarioBackend.repository.UserPreferenceRepository;
import org.interkambio.SistemaInventarioBackend.service.UserPreferenceService;
import org.springframework.stereotype.Service;

@Service
public class UserPreferenceServiceImpl
        extends GenericServiceImpl<UserPreference, UserPreferenceDTO, Long>
        implements UserPreferenceService {

    public UserPreferenceServiceImpl(UserPreferenceRepository repository, UserPreferenceMapper mapper) {
        super(repository, mapper);
    }

    @Override
    protected void setId(UserPreference entity, Long id) {
        entity.setId(id);
    }
}
