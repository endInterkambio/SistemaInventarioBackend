package org.interkambio.SistemaInventarioBackend.criteria;

import lombok.Data;

@Data
public class CustomerSearchCriteria {

    private String search;

    private String name; // Buscar por nombre de persona o empresa
    private String documentNumber;
    private String email;
    private String phoneNumber;
    private String customerType;
}
