package org.interkambio.SistemaInventarioBackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookStockLocationDTO {
    private Long id;
    private Long bookId; // relación real
    private String bookSku; // compatibilidad / migración
    private SimpleIdNameDTO warehouse; // en lugar de warehouseId
    private Integer bookcase;
    private Integer bookcaseFloor;
    private Integer stock;
    private Integer lastStock; // último stock antes de la última modificación
    private String bookCondition;
    private String locationType;
    private OffsetDateTime lastUpdatedAt;
}


