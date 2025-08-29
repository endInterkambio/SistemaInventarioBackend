package org.interkambio.SistemaInventarioBackend.exception;

public class InvalidCustomerFieldException extends IllegalArgumentException {
    public InvalidCustomerFieldException(String message) {
        super(message);
    }
}
