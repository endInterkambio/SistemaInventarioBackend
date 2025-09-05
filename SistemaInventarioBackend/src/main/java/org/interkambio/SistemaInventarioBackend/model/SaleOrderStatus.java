package org.interkambio.SistemaInventarioBackend.model;

public enum SaleOrderStatus {
    PENDING,        // Creada
    IN_PROGRESS,    // Preparando envío
    SHIPPED,        // Enviado
    COMPLETED,      // Completado
    CANCELLED       // Cancelado
}

