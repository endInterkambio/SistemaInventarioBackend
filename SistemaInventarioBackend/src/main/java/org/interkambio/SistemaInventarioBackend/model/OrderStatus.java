package org.interkambio.SistemaInventarioBackend.model;

public enum OrderStatus {
    PENDING,        // Creada
    IN_PROGRESS,    // Preparando envío
    SHIPPED,        // Enviado
    COMPLETED,      // Completado
    CANCELLED       // Cancelado
}

