package org.interkambio.SistemaInventarioBackend.controller;

import org.interkambio.SistemaInventarioBackend.DTO.UserPreferenceDTO;
import org.interkambio.SistemaInventarioBackend.service.UserPreferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class UserPreferenceController {

    private final UserPreferenceService userPreferenceService;

    public UserPreferenceController(UserPreferenceService userPreferenceService) {
        this.userPreferenceService = userPreferenceService;
    }

    // Listar todas las preferencias de usuario
    @GetMapping("user_preferences")
    public List<UserPreferenceDTO> listUsers() {
        return userPreferenceService.listAll();
    }

    // Crear una nueva preferencia de usuario
    @PostMapping("user_preferences")
    public ResponseEntity<UserPreferenceDTO> createUser(@RequestBody UserPreferenceDTO userPreferenceDTO) {
        UserPreferenceDTO created = userPreferenceService.save(userPreferenceDTO);
        return ResponseEntity.ok(created);
    }

    // Actualizar preferencia de usuario existente
    @PutMapping("user_preferences/{id}")
    public ResponseEntity<UserPreferenceDTO> updateUser(@PathVariable Long id, @RequestBody UserPreferenceDTO userPreferenceDTO) {
        return userPreferenceService.update(id, userPreferenceDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar preferencia de usuario por ID
    @DeleteMapping("user_preferences/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean deleted = userPreferenceService.delete(id);
        if (deleted) {
            return ResponseEntity.ok("El registro ID: " + id + " se eliminó correctamente");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
