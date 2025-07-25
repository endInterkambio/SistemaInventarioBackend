package org.interkambio.SistemaInventarioBackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="Users")
@NoArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String username;
    private String password;

    @OneToOne
    @JoinColumn(name="role_id", referencedColumnName = "id")
    private Role role;
}
