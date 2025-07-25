package org.interkambio.SistemaInventarioBackend.service.impl;

import org.interkambio.SistemaInventarioBackend.DTO.UserDTO;
import org.interkambio.SistemaInventarioBackend.mapper.UserMapper;
import org.interkambio.SistemaInventarioBackend.model.User;
import org.interkambio.SistemaInventarioBackend.repository.UserRepository;
import org.interkambio.SistemaInventarioBackend.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends GenericServiceImpl<User, UserDTO, Long> implements UserService {

    public UserServiceImpl(UserRepository userRepository) {
        super(userRepository, new UserMapper());
    }

    @Override
    protected void setId(User entity, Long id) {
        entity.setId(id);
    }
}
