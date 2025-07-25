package org.interkambio.SistemaInventarioBackend.service;

import java.util.List;
import java.util.Optional;

public interface GenericService<TDTO, ID> {
    TDTO save(TDTO dto);
    Optional<TDTO> findById(ID id);
    List<TDTO> findAll();
    Optional<TDTO> update(ID id, TDTO dto);
    boolean delete(ID id);
}
