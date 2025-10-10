package org.interkambio.SistemaInventarioBackend.service.impl;

import org.interkambio.SistemaInventarioBackend.DTO.common.ImportResult;
import org.interkambio.SistemaInventarioBackend.DTO.sales.CustomerContactDTO;
import org.interkambio.SistemaInventarioBackend.DTO.sales.CustomerDTO;
import org.interkambio.SistemaInventarioBackend.criteria.CustomerSearchCriteria;
import org.interkambio.SistemaInventarioBackend.exporter.CustomerExcelExporter;
import org.interkambio.SistemaInventarioBackend.mapper.CustomerMapper;
import org.interkambio.SistemaInventarioBackend.model.Customer;
import org.interkambio.SistemaInventarioBackend.model.CustomerType;
import org.interkambio.SistemaInventarioBackend.model.DocumentType;
import org.interkambio.SistemaInventarioBackend.repository.CustomerRepository;
import org.interkambio.SistemaInventarioBackend.service.CustomerService;
import org.interkambio.SistemaInventarioBackend.specification.CustomerSpecification;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    @Transactional
    public CustomerDTO save(CustomerDTO dto) {
        Customer entity = mapper.toEntity(dto);
        entity.validateFields();

        // Validar email duplicado antes de guardar
        if (entity.getEmail() != null && repository.existsByEmail(entity.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        // Manejo de contactos
        if (entity.getCustomerType() == CustomerType.PERSON) {
            entity.getContacts().clear();
        } else if (entity.getContacts() != null) {
            entity.getContacts().forEach(c -> c.setCustomer(entity));
        }

        try {
            return mapper.toDTO(repository.save(entity));
        } catch (DataIntegrityViolationException ex) {
            throw handleDuplicateConstraint(ex);
        }
    }

    @Override
    @Transactional
    public List<CustomerDTO> saveAll(List<CustomerDTO> dtos) {
        List<Customer> entities = dtos.stream()
                .map(mapper::toEntity)
                .peek(Customer::validateFields)
                .collect(Collectors.toList());

        // Validar emails duplicados contra la BD antes de guardar
        for (Customer customer : entities) {
            if (customer.getEmail() != null && repository.existsByEmail(customer.getEmail())) {
                throw new IllegalArgumentException(
                        "El correo electrónico ya está registrado: " + customer.getEmail()
                );
            }

            if (customer.getCustomerType() == CustomerType.PERSON) {
                customer.getContacts().clear();
            } else if (customer.getContacts() != null) {
                customer.getContacts().forEach(c -> c.setCustomer(customer));
            }
        }

        try {
            return repository.saveAll(entities).stream().map(mapper::toDTO).collect(Collectors.toList());
        } catch (DataIntegrityViolationException ex) {
            throw handleDuplicateConstraint(ex);
        }
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
            if (dto.getEmail() != null && !dto.getEmail().equals(existing.getEmail())
                    && repository.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException("El correo electrónico ya está registrado");
            }

            Customer updated = mapper.toEntity(dto);
            updated.setId(existing.getId());
            updated.validateFields();

            // Manejo de contactos según tipo
            if (updated.getCustomerType() == CustomerType.PERSON) {
                updated.getContacts().clear(); // eliminar contactos si es PERSON
            } else if (updated.getCustomerType() == CustomerType.COMPANY && updated.getContacts() != null) {
                updated.getContacts().forEach(c -> c.setCustomer(updated));
            }

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
                        case "email" -> {
                            String newEmail = value.toString();
                            if (!newEmail.equals(entity.getEmail()) && repository.existsByEmail(newEmail)) {
                                throw new IllegalArgumentException("El correo electrónico ya está registrado");
                            }
                            field.set(entity, newEmail);
                        }
                        default -> field.set(entity, value);
                    }

                } catch (NoSuchFieldException e) {
                    throw new IllegalArgumentException("Campo no válido: " + key);
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    throw new IllegalArgumentException("Valor inválido para campo " + key + ": " + value);
                }
            });

            entity.validateFields();

            try {
                Customer saved = repository.save(entity);
                return mapper.toDTO(saved);
            } catch (DataIntegrityViolationException ex) {
                throw handleDuplicateConstraint(ex);
            }
        });
    }

    @Override
    public Page<CustomerDTO> searchCustomers(CustomerSearchCriteria criteria, Pageable pageable) {
        // Usar Specification con Pageable directamente
        Page<Customer> customerPage = repository.findAll(
                CustomerSpecification.withFilters(criteria),
                pageable
        );

        // Mapear entidades a DTO
        return customerPage.map(mapper::toDTO);
    }

    private RuntimeException handleDuplicateConstraint(DataIntegrityViolationException ex) {
        String msg = ex.getMostSpecificCause().getMessage();
        if (msg != null) {
            if (msg.contains("customers.unique_email")) {
                return new RuntimeException("El correo electrónico del cliente ya está registrado");
            }
            if (msg.contains("customers.dni")) {
                return new RuntimeException("El número de documento ya está registrado para otro cliente");
            }
        }
        return new RuntimeException("Error de integridad de datos: " + ex.getMessage());
    }

    @Override
    public ImportResult<CustomerDTO> importCustomersFromFile(MultipartFile file) {
        return null;
    }
    
    // TODO
    @Override
    public void exportCustomers(java.io.OutputStream os) throws Exception {
        List<CustomerDTO> customers = this.findAll(Pageable.unpaged()).getContent();
        CustomerExcelExporter.exportUnifiedExcel();
    }
}
