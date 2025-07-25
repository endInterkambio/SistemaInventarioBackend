package org.interkambio.SistemaInventarioBackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "User_preferences")
@NoArgsConstructor

public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;
    private String preferenceKey;
    private String preferenceValue;

    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;
}
