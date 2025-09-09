package org.interkambio.SistemaInventarioBackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.sales.ShipmentMethodDTO;
import org.interkambio.SistemaInventarioBackend.criteria.ShipmentMethodSearchCriteria;
import org.interkambio.SistemaInventarioBackend.mapper.ShipmentMethodMapper;
import org.interkambio.SistemaInventarioBackend.model.ShipmentMethod;
import org.interkambio.SistemaInventarioBackend.repository.ShipmentMethodRepository;
import org.interkambio.SistemaInventarioBackend.service.GenericService;
import org.interkambio.SistemaInventarioBackend.service.ShipmentMethodService;
import org.interkambio.SistemaInventarioBackend.specification.ShipmentMethodSpecification;
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
public class ShipmentMethodServiceImpl implements ShipmentMethodService, GenericService<ShipmentMethodDTO, Long> {

    private final ShipmentMethodRepository repository;
    private final ShipmentMethodMapper mapper;

    @Override
    public ShipmentMethodDTO save(ShipmentMethodDTO dto) {
        ShipmentMethod entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public List<ShipmentMethodDTO> saveAll(List<ShipmentMethodDTO> dtos) {
        List<ShipmentMethod> entities = dtos.stream().map(mapper::toEntity).collect(Collectors.toList());
        return repository.saveAll(entities).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<ShipmentMethodDTO> findById(Long id) {
        return repository.findById(id).map(mapper::toDTO);
    }

    @Override
    public List<ShipmentMethodDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<ShipmentMethodDTO> update(Long id, ShipmentMethodDTO dto) {
        return repository.findById(id).map(entity -> {
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            return mapper.toDTO(repository.save(entity));
        });
    }

    @Override
    public boolean delete(Long id) {
        Optional<ShipmentMethod> entityOpt = repository.findById(id);
        if (entityOpt.isPresent()) {
            repository.delete(entityOpt.get());
            return true;
        }
        return false;
    }

    @Override
    public Optional<ShipmentMethodDTO> partialUpdate(Long id, Map<String, Object> updates) {
        return repository.findById(id).map(entity -> {
            updates.forEach((key, value) -> {
                switch (key) {
                    case "name" -> entity.setName((String) value);
                    case "description" -> entity.setDescription((String) value);
                }
            });
            return mapper.toDTO(repository.save(entity));
        });
    }

    /*@Override
    public Page<ShipmentMethodDTO> search(ShipmentMethodSearchCriteria criteria, Pageable pageable) {
        Specification<ShipmentMethod> spec = ShipmentMethodSpecification.withFilters(criteria);
        Page<ShipmentMethod> page = repository.findAll(spec, pageable);
        return new PageImpl<>(page.getContent().stream().map(mapper::toDTO).collect(Collectors.toList()), pageable, page.getTotalElements());
    }*/
}
