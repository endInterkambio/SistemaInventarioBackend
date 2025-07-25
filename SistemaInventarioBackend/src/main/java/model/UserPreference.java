package model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "UserPreference")
@NoArgsConstructor

public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;
    private String preferenceKey;
    private String preferenceValue;

    @OneToOne
    @JoinColumn(name="userId", referencedColumnName = "id")
    private User user;
}
