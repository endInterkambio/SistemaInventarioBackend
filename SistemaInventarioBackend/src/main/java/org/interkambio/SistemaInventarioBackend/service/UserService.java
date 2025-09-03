package org.interkambio.SistemaInventarioBackend.service;

import org.interkambio.SistemaInventarioBackend.DTO.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends GenericService<UserDTO, Long>, UserDetailsService {
    // Nada más: hereda todo
}
