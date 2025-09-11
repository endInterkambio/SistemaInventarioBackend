package org.interkambio.SistemaInventarioBackend.DTO.inventory;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.common.SimpleIdNameDTO;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookStockLocationDTO {
    private Long id;
    private Long bookId; // relación real
    private String bookSku; // compatibilidad / migración
    private String bookCondition;
    private SimpleIdNameDTO warehouse; // en lugar de warehouseId
    private String locationType;
    private Integer bookcase;
    private Integer bookcaseFloor;
    private Integer stock;
    private Integer lastStock; // último stock antes de la última modificación
    private OffsetDateTime lastUpdatedAt;

    // Constructor para saleOrderItem
    public BookStockLocationDTO(Long id, Long bookId, String bookSku,
                                SimpleIdNameDTO warehouse, Integer bookcase, Integer bookcaseFloor,
                                String bookCondition, String locationType, Integer stock) {
        this.id = id;
        this.bookId = bookId;
        this.bookSku = bookSku;
        this.warehouse = warehouse;
        this.bookcase = bookcase;
        this.bookcaseFloor = bookcaseFloor;
        this.bookCondition = bookCondition;
        this.locationType = locationType;
        this.stock = stock;
    }
}


