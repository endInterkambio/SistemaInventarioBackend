package org.interkambio.SistemaInventarioBackend.service.impl;

import org.interkambio.SistemaInventarioBackend.DTO.WarehouseDTO;
import org.interkambio.SistemaInventarioBackend.mapper.WarehouseMapper;
import org.interkambio.SistemaInventarioBackend.model.Warehouse;
import org.interkambio.SistemaInventarioBackend.repository.WarehouseRepository;
import org.interkambio.SistemaInventarioBackend.service.WarehouseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl extends GenericServiceImpl<Warehouse, WarehouseDTO, Long> implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository,
                                WarehouseMapper warehouseMapper) {
        super(warehouseRepository, warehouseMapper);
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    protected void setId(Warehouse entity, Long id) {
        entity.setId(id);
    }

    @Override
    public List<WarehouseDTO> findAll() {
        return warehouseRepository.findAllWithTotalBooks();
    }
}
