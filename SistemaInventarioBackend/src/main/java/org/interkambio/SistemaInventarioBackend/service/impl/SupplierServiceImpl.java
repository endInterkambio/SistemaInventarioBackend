package org.interkambio.SistemaInventarioBackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.purchase.SupplierDTO;
import org.interkambio.SistemaInventarioBackend.criteria.SupplierSearchCriteria;
import org.interkambio.SistemaInventarioBackend.mapper.SupplierMapper;
import org.interkambio.SistemaInventarioBackend.model.Supplier;
import org.interkambio.SistemaInventarioBackend.repository.SupplierRepository;
import org.interkambio.SistemaInventarioBackend.service.GenericService;
import org.interkambio.SistemaInventarioBackend.service.SupplierService;
import org.interkambio.SistemaInventarioBackend.specification.SupplierSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierServiceImpl implements SupplierService, GenericService<SupplierDTO, Long> {

    private final SupplierRepository repository;
    private final SupplierMapper mapper;

    @Override
    public SupplierDTO save(SupplierDTO dto) {
        Supplier entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public List<SupplierDTO> saveAll(List<SupplierDTO> dtos) {
        List<Supplier> entities = dtos.stream().map(mapper::toEntity).collect(Collectors.toList());
        return repository.saveAll(entities).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<SupplierDTO> findById(Long id) {
        return repository.findById(id).map(mapper::toDTO);
    }

    @Override
    public List<SupplierDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<SupplierDTO> update(Long id, SupplierDTO dto) {
        return repository.findById(id).map(entity -> {
            entity.setName(dto.getName());
            entity.setContactPerson(dto.getContactPerson());
            entity.setEmail(dto.getEmail());
            entity.setPhoneNumber(dto.getPhoneNumber());
            entity.setAddress(dto.getAddress());
            return mapper.toDTO(repository.save(entity));
        });
    }

    @Override
    public boolean delete(Long id) {
        Optional<Supplier> supplierOpt = repository.findById(id);
        if (supplierOpt.isPresent()) {
            repository.delete(supplierOpt.get());
            return true;
        }
        return false;
    }

    @Override
    public Optional<SupplierDTO> partialUpdate(Long id, Map<String, Object> updates) {
        return repository.findById(id).map(entity -> {
            updates.forEach((key, value) -> {
                switch (key) {
                    case "name" -> entity.setName((String) value);
                    case "contactPerson" -> entity.setContactPerson((String) value);
                    case "email" -> entity.setEmail((String) value);
                    case "phoneNumber" -> entity.setPhoneNumber((String) value);
                    case "address" -> entity.setAddress((String) value);
                }
            });
            return mapper.toDTO(repository.save(entity));
        });
    }

    // Método de búsqueda con filtros
    public Page<SupplierDTO> search(SupplierSearchCriteria criteria, Pageable pageable) {
        Specification<Supplier> spec = SupplierSpecification.withFilters(criteria);
        Page<Supplier> page = repository.findAll(spec, pageable);
        return new PageImpl<>(
                page.getContent().stream().map(mapper::toDTO).collect(Collectors.toList()),
                pageable,
                page.getTotalElements()
        );
    }
}
