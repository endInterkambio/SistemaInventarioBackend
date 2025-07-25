package org.interkambio.SistemaInventarioBackend.service.impl;

import org.interkambio.SistemaInventarioBackend.mapper.GenericMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
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
