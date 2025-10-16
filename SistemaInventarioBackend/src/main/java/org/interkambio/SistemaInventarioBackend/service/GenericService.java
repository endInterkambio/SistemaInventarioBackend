package org.interkambio.SistemaInventarioBackend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GenericService<TDTO, ID> {
    TDTO save(TDTO dto);
    List<TDTO> saveAll(List<TDTO> dtos);
    Optional<TDTO> findById(ID id);
    List<TDTO> findAll();
    Optional<TDTO> update(ID id, TDTO dto);
    boolean delete(ID id);
    Optional<TDTO> partialUpdate(ID id, Map<String, Object> updates);
    // TODO: Firma genérica para búsqueda
    // Page<TDTO> search(C criteria, Pageable pageable);
}
