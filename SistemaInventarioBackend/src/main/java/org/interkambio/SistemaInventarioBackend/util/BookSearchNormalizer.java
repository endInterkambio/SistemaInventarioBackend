package org.interkambio.SistemaInventarioBackend.util;

import java.text.Normalizer;

public class BookSearchNormalizer {

    public static String normalize(String input) {
        if (input == null) return "";
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")       // elimina tildes
                .replaceAll("[\\W_]+", " ")     // reemplaza símbolos raros (&, |, -, etc.)
                .replaceAll("\\s+", " ")        // colapsa múltiples espacios
                .toLowerCase()
                .trim();
    }
}

