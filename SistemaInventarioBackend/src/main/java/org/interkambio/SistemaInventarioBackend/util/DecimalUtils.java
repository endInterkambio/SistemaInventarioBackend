package org.interkambio.SistemaInventarioBackend.util;

import java.math.BigDecimal;

public class DecimalUtils {

    public static BigDecimal toBigDecimal(Object value) {
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        } else if (value instanceof String) {
            try {
                return new BigDecimal(((String) value).replace(",", "."));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Formato decimal inválido: " + value);
            }
        } else {
            throw new IllegalArgumentException("Tipo inválido para BigDecimal: " + value);
        }
    }
}
