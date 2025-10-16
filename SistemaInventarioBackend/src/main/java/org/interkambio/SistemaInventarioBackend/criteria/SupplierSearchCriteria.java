package org.interkambio.SistemaInventarioBackend.criteria;

import lombok.Data;

@Data
public class SupplierSearchCriteria {

    private String search;

    private String name;
    private String contactPerson;
    private String email;
    private String phoneNumber;
    private String address;
}
