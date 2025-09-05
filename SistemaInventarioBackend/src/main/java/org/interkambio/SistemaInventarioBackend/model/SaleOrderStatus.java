package org.interkambio.SistemaInventarioBackend.model;

public enum SaleOrderStatus {
    PENDING,        // Orden creada, sin acción
    IN_PROGRESS,    // Preparando envío
    SHIPPED,        // Enviado
    INVOICED,       // Facturado SIN USO ACTUALMENTE
    PAID,           // Pagado
    CANCELLED       // Cancelado
}
