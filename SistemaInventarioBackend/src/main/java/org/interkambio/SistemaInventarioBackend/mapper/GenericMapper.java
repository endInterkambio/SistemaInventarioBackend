package org.interkambio.SistemaInventarioBackend.mapper;

public interface GenericMapper<TEntity, TDTO> {
    TDTO toDTO(TEntity entity);
    TEntity toEntity(TDTO dto);
}
