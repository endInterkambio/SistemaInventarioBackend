package org.interkambio.SistemaInventarioBackend.DTO.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportResult<T> {
    private boolean success;
    private String message;
    private List<T> data;
}



