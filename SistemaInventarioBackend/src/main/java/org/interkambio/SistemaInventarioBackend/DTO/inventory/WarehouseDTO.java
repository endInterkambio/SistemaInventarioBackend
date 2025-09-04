package org.interkambio.SistemaInventarioBackend.DTO.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class WarehouseDTO {
    private Long id;
    private String name;
    private String location;
    private String description;

    // 🔹 campo referencial
    private Long totalBooks;
}

