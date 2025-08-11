package org.interkambio.SistemaInventarioBackend.criteria;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class InventoryTransactionSearchCriteria {

    private String bookSku;        // Filtrar por SKU
    private Long fromLocationId;   // Ubicación de origen
    private Long toLocationId;     // Ubicación de destino
    private String transactionType; // Tipo de transacción (ej: TRANSFER, ADJUSTMENT, SALE)
    private String reason;         // Motivo (contiene texto)
    private Long userId;           // Usuario que ejecutó la transacción

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fromDate; // Fecha inicial

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime toDate;   // Fecha final
}
