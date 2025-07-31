package org.interkambio.SistemaInventarioBackend.service.impl;

import org.interkambio.SistemaInventarioBackend.DTO.WarehouseDTO;
import org.interkambio.SistemaInventarioBackend.mapper.WarehouseMapper;
import org.interkambio.SistemaInventarioBackend.model.Warehouse;
import org.interkambio.SistemaInventarioBackend.repository.WarehouseRepository;
import org.interkambio.SistemaInventarioBackend.service.WarehouseService;
import org.springframework.stereotype.Service;

@Service
public class WarehouseServiceImpl extends GenericServiceImpl<Warehouse, WarehouseDTO, Long> implements WarehouseService {

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository) {
        super(warehouseRepository, new WarehouseMapper());
    }

    @Override
    protected void setId(Warehouse entity, Long id) {
        entity.setId(id);
    }
}
