package org.interkambio.SistemaInventarioBackend.service.impl;

import org.interkambio.SistemaInventarioBackend.DTO.RoleDTO;
import org.interkambio.SistemaInventarioBackend.mapper.RoleMapper;
import org.interkambio.SistemaInventarioBackend.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.interkambio.SistemaInventarioBackend.repository.RoleRepository;
import org.interkambio.SistemaInventarioBackend.service.RoleService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleDTO save(RoleDTO roleDTO) {
        Role role = RoleMapper.toEntity(roleDTO);
        Role saved = roleRepository.save(role);
        return RoleMapper.toDTO(saved);
    }

    @Override
    public List<RoleDTO> listAll() {
        return roleRepository.findAll()
                .stream()
                .map(RoleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoleDTO> searchById(Long id) {
        return roleRepository.findById(id)
                .map(RoleMapper::toDTO);
    }

    @Override
    public Optional<RoleDTO> update(Long id, RoleDTO roleDTO) {
        return roleRepository.findById(id).map(existente -> {
            Role role = RoleMapper.toEntity(roleDTO);
            role.setId(id); // aseguramos que se actualice el existente
            Role updated = roleRepository.save(role);
            return RoleMapper.toDTO(updated);
        });
    }

    @Override
    public void delete(Long id) {
        roleRepository.deleteById(id);
    }
}
