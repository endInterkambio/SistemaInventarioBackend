package org.interkambio.SistemaInventarioBackend.service.impl;

import org.interkambio.SistemaInventarioBackend.DTO.sales.CustomerContactDTO;
import org.interkambio.SistemaInventarioBackend.mapper.CustomerContactMapper;
import org.interkambio.SistemaInventarioBackend.model.Customer;
import org.interkambio.SistemaInventarioBackend.model.CustomerContact;
import org.interkambio.SistemaInventarioBackend.model.CustomerType;
import org.interkambio.SistemaInventarioBackend.repository.CustomerContactRepository;
import org.interkambio.SistemaInventarioBackend.repository.CustomerRepository;
import org.interkambio.SistemaInventarioBackend.service.CustomerContactService;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final CustomerRepository customerRepository;
    private final CustomerContactMapper mapper;

    public CustomerContactServiceImpl(CustomerContactRepository repository, CustomerRepository customerRepository, CustomerContactMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerContactDTO save(CustomerContactDTO dto) {
        CustomerContact entity = mapper.toEntity(dto);

        // Normalizar email
        if (entity.getEmail() != null) {
            entity.setEmail(entity.getEmail().trim().toLowerCase());
        }

        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            if (!CustomerType.COMPANY.equals(customer.getCustomerType())) {
                throw new RuntimeException("Solo se puede asignar contacto a clientes tipo COMPANY");
            }

            entity.setCustomer(customer);
        }

        try {
            return mapper.toDTO(repository.save(entity));
        } catch (DataIntegrityViolationException ex) {
            throw new RuntimeException("El email ya está registrado para otro contacto");
        }
    }

    @Override
    public List<CustomerContactDTO> saveAll(List<CustomerContactDTO> dtos) {
        List<CustomerContact> entities = dtos.stream().map(dto -> {
            CustomerContact entity = mapper.toEntity(dto);
            if (dto.getCustomerId() != null) {
                Customer customer = customerRepository.findById(dto.getCustomerId())
                        .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
                if (!CustomerType.COMPANY.equals(customer.getCustomerType())) {
                    throw new RuntimeException("Solo se puede asignar contacto a clientes tipo COMPANY");
                }

                entity.setCustomer(customer);
            }
            return entity;
        }).collect(Collectors.toList());

        return repository.saveAll(entities).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
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

            if (dto.getCustomerId() != null) {
                Customer customer = customerRepository.findById(dto.getCustomerId())
                        .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

                if (!CustomerType.COMPANY.equals(customer.getCustomerType())) {
                    throw new RuntimeException("Solo se puede asignar contacto a clientes tipo COMPANY");
                }

                updated.setCustomer(customer);
            } else {
                updated.setCustomer(existing.getCustomer());
            }

            // Normalizar email
            if (updated.getEmail() != null) {
                updated.setEmail(updated.getEmail().trim().toLowerCase());
            }

            try {
                return mapper.toDTO(repository.save(updated));
            } catch (DataIntegrityViolationException ex) {
                throw new RuntimeException("El email ya está registrado para otro contacto");
            }
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
                    if ("customerId".equals(key)) {
                        Long customerId = Long.valueOf(value.toString());
                        Customer customer = customerRepository.findById(customerId)
                                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

                        if (!CustomerType.COMPANY.equals(customer.getCustomerType())) {
                            throw new RuntimeException("Solo se puede asignar contacto a clientes tipo COMPANY");
                        }

                        entity.setCustomer(customer);

                    } else if ("email".equals(key)) {
                        // Normalizar email
                        entity.setEmail(value.toString().trim().toLowerCase());

                    } else {
                        Field field = CustomerContact.class.getDeclaredField(key);
                        field.setAccessible(true);
                        if (field.getType() == String.class) {
                            field.set(entity, value.toString());
                        } else {
                            field.set(entity, value);
                        }
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    // Ignorar campos inválidos
                }
            });

            try {
                CustomerContact saved = repository.save(entity);
                return mapper.toDTO(saved);
            } catch (DataIntegrityViolationException ex) {
                throw new RuntimeException("El email ya está registrado para otro contacto");
            }
        });
    }

}

