package org.interkambio.SistemaInventarioBackend.DTO.sales;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private Long id;

    @NotBlank(message = "El tipo de cliente es obligatorio")
    private String customerType; // Ej: PERSON o COMPANY

    @NotBlank(message = "El tipo de documento es obligatorio")
    private String documentType; // Ej: DNI, RUC, PASSPORT

    @NotBlank(message = "El número de documento es obligatorio")
    @Size(min = 8, max = 11, message = "El número de documento debe tener entre 8 y 11 caracteres")
    @Pattern(regexp = "\\d+", message = "El número de documento solo puede contener dígitos")
    private String documentNumber;

    @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
    private String name;

    @Size(max = 150, message = "La razón social no debe superar los 150 caracteres")
    private String companyName;

    @Email(message = "El correo electrónico no tiene un formato válido")
    @Size(max = 120, message = "El correo electrónico no debe superar los 120 caracteres")
    private String email;

    @Pattern(regexp = "\\+?\\d{7,12}", message = "El teléfono debe contener entre 7 y 12 dígitos, puede iniciar con +")
    private String phoneNumber;

    @Size(max = 200, message = "La dirección no debe superar los 200 caracteres")
    private String address;

    private List<CustomerContactDTO> contacts;

    /**
     * Devuelve un nombre amigable según el tipo de cliente
     */
    public String getDisplayName() {
        if ("COMPANY".equalsIgnoreCase(customerType) && companyName != null) {
            return companyName;
        }
        return name;
    }
}
