package org.interkambio.SistemaInventarioBackend.criteria;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class BookStockAdjustmentSearchCriteria {

    private String bookSku;        // Para buscar por SKU
    private Long locationId;       // Filtrar por ubicación
    private Long performedById;    // Usuario que hizo el ajuste
    private String reason;         // Motivo del ajuste (contiene)

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fromDate; // Fecha mínima

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime toDate;   // Fecha máxima
}
