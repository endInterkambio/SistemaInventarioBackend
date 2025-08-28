package org.interkambio.SistemaInventarioBackend.service.impl;
import org.interkambio.SistemaInventarioBackend.DTO.CustomerContactDTO;
import org.interkambio.SistemaInventarioBackend.mapper.CustomerContactMapper;
import org.interkambio.SistemaInventarioBackend.model.CustomerContact;
import org.interkambio.SistemaInventarioBackend.repository.CustomerContactRepository;
import org.interkambio.SistemaInventarioBackend.service.CustomerContactService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerContactServiceImpl implements CustomerContactService {

    private final CustomerContactRepository repository;
    private final CustomerContactMapper mapper;

    public CustomerContactServiceImpl(CustomerContactRepository repository, CustomerContactMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CustomerContactDTO save(CustomerContactDTO dto) {
        CustomerContact entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public List<CustomerContactDTO> saveAll(List<CustomerContactDTO> dtos) {
        List<CustomerContact> entities = dtos.stream().map(mapper::toEntity).collect(Collectors.toList());
        return repository.saveAll(entities).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerContactDTO> findById(Long id) {
        return repository.findById(id).map(mapper::toDTO);
    }

    @Override
    public List<CustomerContactDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerContactDTO> update(Long id, CustomerContactDTO dto) {
        return repository.findById(id).map(existing -> {
            CustomerContact updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.setCustomer(existing.getCustomer());
            return mapper.toDTO(repository.save(updated));
        });
    }

    @Override
    public boolean delete(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }

    @Override
    public Optional<CustomerContactDTO> partialUpdate(Long id, Map<String, Object> updates) {
        return repository.findById(id).map(entity -> {
            updates.forEach((key, value) -> {
                if (value == null) return;

                try {
                    Field field = CustomerContact.class.getDeclaredField(key);
                    field.setAccessible(true);

                    if (field.getType() == String.class) {
                        field.set(entity, value.toString());
                    } else {
                        field.set(entity, value);
                    }

                } catch (NoSuchFieldException | IllegalAccessException e) {
                    // Ignorar campos inválidos
                }
            });

            CustomerContact saved = repository.save(entity);
            return mapper.toDTO(saved);
        });
    }
}

