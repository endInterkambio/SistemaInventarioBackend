package org.interkambio.SistemaInventarioBackend.model;

import lombok.Getter;

@Getter
public enum TransactionType {
    PURCHASE,
    SALE,
    ADJUSTMENT,
    RETURN_IN,
    RETURN_OUT,
    TRANSFER
}
