package service;

import DTO.RoleDTO;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    RoleDTO save(RoleDTO roleDTO);
    List<RoleDTO> listAll();
    Optional<RoleDTO> searchById(Long id);
    Optional<RoleDTO> update(Long id, RoleDTO dto);
    void delete(Long id);
}
