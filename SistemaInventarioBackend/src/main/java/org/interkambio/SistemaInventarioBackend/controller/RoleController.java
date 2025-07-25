package org.interkambio.SistemaInventarioBackend.controller;

import org.interkambio.SistemaInventarioBackend.DTO.RoleDTO;
import org.interkambio.SistemaInventarioBackend.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")

public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // Listar todos los roles
    @GetMapping("roles")
    public List<RoleDTO> listUsers() {
        return roleService.listAll();
    }

    // Crear un nuevo rol
    @PostMapping("roles")
    public ResponseEntity<RoleDTO> createUser(@RequestBody RoleDTO roleDTO) {
        RoleDTO created = roleService.save(roleDTO);
        return ResponseEntity.ok(created);
    }

    // Actualizar rol existente
    @PutMapping("roles/{id}")
    public ResponseEntity<RoleDTO> updateUser(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        return roleService.update(id, roleDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar rol por ID
    @DeleteMapping("roles/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean deleted = roleService.delete(id);
        if (deleted) {
            return ResponseEntity.ok("El registro ID: " + id + " se eliminó correctamente");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
