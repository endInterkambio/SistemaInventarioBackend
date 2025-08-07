package org.interkambio.SistemaInventarioBackend.service.impl;

import jakarta.transaction.Transactional;
import org.interkambio.SistemaInventarioBackend.mapper.GenericMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericServiceImpl<TEntity, TDTO, ID>
        implements org.interkambio.SistemaInventarioBackend.service.GenericService<TDTO, ID> {

    protected final JpaRepository<TEntity, ID> repository;
    protected final GenericMapper<TEntity, TDTO> mapper;

    protected GenericServiceImpl(JpaRepository<TEntity, ID> repository, GenericMapper<TEntity, TDTO> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public TDTO save(TDTO dto) {
        TEntity entity = mapper.toEntity(dto);
        TEntity saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    @Transactional
    public List<TDTO> saveAll(List<TDTO> dtos) {
        return dtos.stream()
                .map(this::save)
                .collect(Collectors.toList());
    }


    @Override
    public Optional<TDTO> findById(ID id) {
        return repository.findById(id).map(mapper::toDTO);
    }

    @Override
    public List<TDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TDTO> update(ID id, TDTO dto) {
        return repository.findById(id).map(existing -> {
            TEntity entityToUpdate = mapper.toEntity(dto);
            setId(entityToUpdate, id);
            return mapper.toDTO(repository.save(entityToUpdate));
        });
    }

    @Override
    public Optional<TDTO> partialUpdate(ID id, Map<String, Object> updates) {
        return repository.findById(id).map(existingEntity -> {
            updates.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(existingEntity.getClass(), key);
                if (field != null) {
                    field.setAccessible(true);

                    Class<?> fieldType = field.getType();

                    Object convertedValue = convertValue(value, fieldType);
                    ReflectionUtils.setField(field, existingEntity, convertedValue);
                }
            });

            TEntity savedEntity = repository.save(existingEntity);
            return mapper.toDTO(savedEntity);
        });
    }

    private Object convertValue(Object value, Class<?> targetType) {
        if (value == null) return null;

        if (targetType.equals(BigDecimal.class)) {
            if (value instanceof Number) {
                return BigDecimal.valueOf(((Number) value).doubleValue());
            } else if (value instanceof String) {
                try {
                    return new BigDecimal((String) value);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid BigDecimal value: " + value);
                }
            }
        }
        return value;
    }


    @Override
    public boolean delete(ID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }


    // Debes implementar esta en las clases hijas
    protected abstract void setId(TEntity entity, ID id);
}
