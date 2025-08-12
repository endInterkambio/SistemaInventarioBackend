package org.interkambio.SistemaInventarioBackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookStockAdjustmentDTO {
    private Long id;
    private String bookSku; // Para identificar el libro
    private Long locationId; // 🔹 ID directo de la ubicación
    private Integer adjustmentQuantity;
    private String reason;
    private SimpleIdNameDTO performedBy;
    private LocalDateTime performedAt;
}

