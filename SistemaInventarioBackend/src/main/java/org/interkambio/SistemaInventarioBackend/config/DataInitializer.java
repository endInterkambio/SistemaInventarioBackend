/*package org.interkambio.SistemaInventarioBackend.config;

import org.interkambio.SistemaInventarioBackend.model.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.interkambio.SistemaInventarioBackend.repository.UserRepository;
import org.interkambio.SistemaInventarioBackend.repository.RoleRepository;
import org.interkambio.SistemaInventarioBackend.model.User;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        return args -> {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ADMIN")));

            userRepository.findByUsername("Admin").ifPresentOrElse(admin -> {
                // Usuario existe → actualizar contraseña
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole(adminRole);
                userRepository.save(admin);
                System.out.println("✅ Contraseña del Admin actualizada con BCrypt");
            }, () -> {
                // Usuario no existe → crear nuevo
                User admin = new User();
                admin.setUsername("Admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole(adminRole);
                userRepository.save(admin);
                System.out.println("✅ Usuario Admin creado con contraseña encriptada");
            });
        };
    }

}*/

