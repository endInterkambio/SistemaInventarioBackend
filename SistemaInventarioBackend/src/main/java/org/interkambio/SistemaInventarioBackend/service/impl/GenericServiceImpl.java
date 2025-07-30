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
                    ReflectionUtils.setField(field, existingEntity, value);
                }
            });

            TEntity savedEntity = repository.save(existingEntity);
            return mapper.toDTO(savedEntity);
        });
    }

    // Utilidad de conversión de tipos
    private Object convertValueIfNeeded(Class<?> targetType, Object value) {
        if (value == null) return null;
        if (targetType.isAssignableFrom(value.getClass())) return value;

        try {
            if (targetType == BigDecimal.class) return new BigDecimal(value.toString());
            if (targetType == Integer.class || targetType == int.class) return Integer.parseInt(value.toString());
            if (targetType == Long.class || targetType == long.class) return Long.parseLong(value.toString());
            if (targetType == Double.class || targetType == double.class) return Double.parseDouble(value.toString());
            if (targetType == Boolean.class || targetType == boolean.class) return Boolean.parseBoolean(value.toString());
            if (targetType == String.class) return value.toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al convertir campo: " + value + " a tipo " + targetType.getName(), e);
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
