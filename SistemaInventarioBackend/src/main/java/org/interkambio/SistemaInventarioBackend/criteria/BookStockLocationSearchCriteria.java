package org.interkambio.SistemaInventarioBackend.criteria;

import lombok.Data;

@Data
public class BookStockLocationSearchCriteria {
    private String bookSku;         // filtra por SKU del libro
    private Long warehouseId;       // id del warehouse
    private Integer bookcase;       // número de estantería (bookcase)
    private Integer bookcaseFloor;  // piso (bookcase_floor)
    private Integer minStock;       // stock mínimo
    private Integer maxStock;       // stock máximo
    private String bookCondition;   // A, B, C, D, X, U
}
