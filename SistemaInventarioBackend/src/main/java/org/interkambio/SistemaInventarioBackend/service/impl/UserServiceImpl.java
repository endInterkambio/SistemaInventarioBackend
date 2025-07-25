package org.interkambio.SistemaInventarioBackend.service.impl;

import org.interkambio.SistemaInventarioBackend.DTO.UserDTO;
import org.interkambio.SistemaInventarioBackend.mapper.UserMapper;
import org.interkambio.SistemaInventarioBackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.interkambio.SistemaInventarioBackend.repository.UserRepository;
import org.interkambio.SistemaInventarioBackend.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO save(UserDTO userDTO) {
        User user = UserMapper.toEntity(userDTO);
        User saved = userRepository.save(user);
        return UserMapper.toDTO(saved);
    }

    @Override
    public List<UserDTO> listAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> searchById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toDTO);
    }

    @Override
    public Optional<UserDTO> update(Long id, UserDTO userDTO) {
        return userRepository.findById(id).map(existente -> {
            User user = UserMapper.toEntity(userDTO);
            user.setId(id); // aseguramos que se actualice el existente
            User updated = userRepository.save(user);
            return UserMapper.toDTO(updated);
        });
    }

    @Override
    public boolean delete(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
