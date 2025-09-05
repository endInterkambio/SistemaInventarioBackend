package org.interkambio.SistemaInventarioBackend.model;

public enum PaymentStatus {
    UNPAID,          // Sin pagos
    PARTIALLY_PAID,  // Con pagos parciales
    PAID,            // Totalmente pagado
    INVOICED         // Facturado (si lo manejas en tu flujo)
}

