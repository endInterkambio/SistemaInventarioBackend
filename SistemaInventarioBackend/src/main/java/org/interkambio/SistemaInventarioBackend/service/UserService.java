package org.interkambio.SistemaInventarioBackend.service;

import org.interkambio.SistemaInventarioBackend.DTO.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO save(UserDTO userDTO);
    List<UserDTO> listAll();
    Optional<UserDTO> searchById(Long id);
    Optional<UserDTO> update(Long id, UserDTO dto);
    boolean delete(Long id);
}
