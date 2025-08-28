package org.interkambio.SistemaInventarioBackend.service.impl;

import org.interkambio.SistemaInventarioBackend.DTO.CustomerDTO;
import org.interkambio.SistemaInventarioBackend.mapper.CustomerMapper;
import org.interkambio.SistemaInventarioBackend.model.Customer;
import org.interkambio.SistemaInventarioBackend.model.CustomerType;
import org.interkambio.SistemaInventarioBackend.model.DocumentType;
import org.interkambio.SistemaInventarioBackend.repository.CustomerRepository;
import org.interkambio.SistemaInventarioBackend.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public CustomerServiceImpl(CustomerRepository repository, CustomerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CustomerDTO save(CustomerDTO dto) {
        Customer entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public List<CustomerDTO> saveAll(List<CustomerDTO> dtos) {
        List<Customer> entities = dtos.stream().map(mapper::toEntity).collect(Collectors.toList());
        return repository.saveAll(entities).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> findById(Long id) {
        return repository.findById(id).map(mapper::toDTO);
    }

    @Override
    public List<CustomerDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> update(Long id, CustomerDTO dto) {
        return repository.findById(id).map(existing -> {
            Customer updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
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
    public Optional<CustomerDTO> partialUpdate(Long id, Map<String, Object> updates) {
        return repository.findById(id).map(entity -> {
            updates.forEach((key, value) -> {
                if (value == null) return;

                try {
                    Field field = Customer.class.getDeclaredField(key);
                    field.setAccessible(true);

                    if ("customerType".equals(key)) {
                        field.set(entity, CustomerType.valueOf(value.toString()));
                    } else if ("documentType".equals(key)) {
                        field.set(entity, DocumentType.valueOf(value.toString()));
                    } else if (field.getType() == String.class) {
                        field.set(entity, value.toString());
                    } else {
                        field.set(entity, value);
                    }

                } catch (NoSuchFieldException | IllegalAccessException e) {
                    // Ignorar campos inválidos
                }
            });

            Customer saved = repository.save(entity);
            return mapper.toDTO(saved);
        });
    }
}
