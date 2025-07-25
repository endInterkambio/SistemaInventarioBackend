package controller;

import DTO.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.UserService;

import java.util.List;

@RestController
@RequestMapping("api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Listar todos los usuarios
    @GetMapping("users")
    public List<UserDTO> listUsers() {
        return userService.listAll();
    }

    // Crear un nuevo usuario
    @PostMapping("users")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO created = userService.save(userDTO);
        return ResponseEntity.ok(created);
    }

    // Actualizar usuario existente
    @PutMapping("users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return userService.update(id, userDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar usuario por ID
    @DeleteMapping("users//{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok("El registro ID: " + id + "se eliminó correctamente");
    }
}
