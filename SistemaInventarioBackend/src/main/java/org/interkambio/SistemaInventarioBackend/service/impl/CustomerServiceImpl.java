package org.interkambio.SistemaInventarioBackend.service.impl;

import org.interkambio.SistemaInventarioBackend.DTO.sales.CustomerDTO;
import org.interkambio.SistemaInventarioBackend.criteria.CustomerSearchCriteria;
import org.interkambio.SistemaInventarioBackend.mapper.CustomerMapper;
import org.interkambio.SistemaInventarioBackend.model.Customer;
import org.interkambio.SistemaInventarioBackend.model.CustomerType;
import org.interkambio.SistemaInventarioBackend.model.DocumentType;
import org.interkambio.SistemaInventarioBackend.repository.CustomerRepository;
import org.interkambio.SistemaInventarioBackend.service.CustomerService;
import org.interkambio.SistemaInventarioBackend.specification.CustomerSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        entity.validateFields(); // asegura coherencia antes de guardar
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public List<CustomerDTO> saveAll(List<CustomerDTO> dtos) {
        List<Customer> entities = dtos.stream()
                .map(mapper::toEntity)
                .peek(Customer::validateFields) // valida cada entidad
                .collect(Collectors.toList());
        return repository.saveAll(entities).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> findById(Long id) {
        return repository.findById(id).map(mapper::toDTO);
    }

    // Listing without pagination
    @Override
    public List<CustomerDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Page<CustomerDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Override
    public Optional<CustomerDTO> update(Long id, CustomerDTO dto) {
        return repository.findById(id).map(existing -> {
            Customer updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.validateFields(); // validar coherencia
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

                    switch (key) {
                        case "customerType" -> field.set(entity, CustomerType.valueOf(value.toString()));
                        case "documentType" -> field.set(entity, DocumentType.valueOf(value.toString()));
                        case "name", "companyName" -> field.set(entity, value.toString());
                        default -> field.set(entity, value);
                    }

                } catch (NoSuchFieldException e) {
                    throw new IllegalArgumentException("Campo no válido: " + key);
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    throw new IllegalArgumentException("Valor inválido para campo " + key + ": " + value);
                }
            });

            // Llamada a validateFields() lanza excepción si hay incoherencia
            entity.validateFields();

            Customer saved = repository.save(entity);
            return mapper.toDTO(saved);
        });
    }

    @Override
    public Page<CustomerDTO> searchCustomers(CustomerSearchCriteria criteria, int page, int size) {
        // Crear PageRequest
        PageRequest pageRequest = PageRequest.of(page, size);

        // Usar Specification para filtrar clientes
        Page<Customer> customerPage = repository.findAll(
                CustomerSpecification.withFilters(criteria),
                pageRequest
        );

        // Mapear entidades a DTO
        return customerPage.map(mapper::toDTO);
    }

}
