package org.interkambio.SistemaInventarioBackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.interkambio.SistemaInventarioBackend.exception.InvalidCustomerFieldException;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", nullable = false)
    private CustomerType customerType; // PERSON o COMPANY

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private DocumentType documentType;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "name")
    private String name; // solo se usa si customerType == PERSON

    @Column(name = "company_name")
    private String companyName; // solo se usa si customerType == COMPANY

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerContact> contacts = new ArrayList<>();

    @PrePersist
    @PreUpdate
    public void validateFields() {
        if (customerType == CustomerType.PERSON) {
            if (companyName != null && !companyName.isBlank()) {
                throw new InvalidCustomerFieldException(
                        "El campo 'companyName' no debe enviarse para clientes PERSON."
                );
            }
            this.companyName = null; // limpieza opcional
        } else if (customerType == CustomerType.COMPANY) {
            if (name != null && !name.isBlank()) {
                throw new InvalidCustomerFieldException(
                        "El campo 'name' no debe enviarse para clientes COMPANY. Use 'companyName'."
                );
            }
            this.name = null; // limpieza opcional
        }
    }


}
