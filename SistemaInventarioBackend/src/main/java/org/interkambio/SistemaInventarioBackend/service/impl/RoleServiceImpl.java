package org.interkambio.SistemaInventarioBackend.service.impl;

import org.interkambio.SistemaInventarioBackend.DTO.user.RoleDTO;
import org.interkambio.SistemaInventarioBackend.mapper.RoleMapper;
import org.interkambio.SistemaInventarioBackend.model.Role;
import org.interkambio.SistemaInventarioBackend.repository.RoleRepository;
import org.interkambio.SistemaInventarioBackend.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends GenericServiceImpl<Role, RoleDTO, Long> implements RoleService {

    public RoleServiceImpl(RoleRepository roleRepository) {
        super(roleRepository, new RoleMapper());
    }

    @Override
    protected void setId(Role entity, Long id) {
        entity.setId(id);
    }
}
