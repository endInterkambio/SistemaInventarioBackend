package org.interkambio.SistemaInventarioBackend.importer.util;

import org.apache.poi.ss.usermodel.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DataFieldParser {

    public static String parseString(String value) {
        return (value != null && !value.trim().isEmpty()) ? value.trim() : null;
    }

    public static Integer parseInt(String value) {
        try {
            return parseString(value) != null ? Integer.parseInt(value.trim()) : null;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Se esperaba un número entero, pero se encontró: '" + value + "'");
        }
    }

    public static Long parseLong(String value) {
        try {
            return parseString(value) != null ? Long.parseLong(value.trim()) : null;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Se esperaba un número entero largo (Long), pero se encontró: '" + value + "'");
        }
    }

    public static BigDecimal parseBigDecimal(String value) {
        try {
            return parseString(value) != null ? new BigDecimal(value.trim()) : null;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Se esperaba un número decimal, pero se encontró: '" + value + "'");
        }
    }

    public static LocalDateTime parseDateTime(String value) {
        if (parseString(value) == null) return null;

        String trimmed = value.trim();

        try {
            // ISO_LOCAL_DATE_TIME: 2025-08-01T10:30:00
            return LocalDateTime.parse(trimmed);
        } catch (Exception e1) {
            try {
                // yyyy-MM-dd HH:mm:ss
                DateTimeFormatter dtWithSpace = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return LocalDateTime.parse(trimmed, dtWithSpace);
            } catch (Exception e2) {
                try {
                    // yyyy-MM-dd (solo fecha)
                    LocalDate dateOnly = LocalDate.parse(trimmed);
                    return dateOnly.atStartOfDay(); // le pone 00:00:00
                } catch (Exception e3) {
                    throw new IllegalArgumentException(
                            "Formato de fecha y hora inválido. Se esperaba 'yyyy-MM-ddTHH:mm:ss', 'yyyy-MM-dd HH:mm:ss' o 'yyyy-MM-dd', pero se encontró: '" + trimmed + "'"
                    );
                }
            }
        }
    }

    public static OffsetDateTime parseOffsetDateTime(String value) {
        if (parseString(value) == null) return null;

        String trimmed = value.trim();

        try {
            // Caso 1: ISO con offset incluido → 2025-09-03T10:19:14Z o 2025-09-03T10:19:14+02:00
            return OffsetDateTime.parse(trimmed);
        } catch (Exception e1) {
            try {
                // Caso 2: Formato con espacio → yyyy-MM-dd HH:mm:ss
                DateTimeFormatter dtWithSpace = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime ldt = LocalDateTime.parse(trimmed, dtWithSpace);
                return ldt.atOffset(ZoneOffset.UTC); // por defecto UTC
            } catch (Exception e2) {
                try {
                    // Caso 3: Solo fecha → yyyy-MM-dd
                    LocalDate dateOnly = LocalDate.parse(trimmed);
                    return dateOnly.atStartOfDay().atOffset(ZoneOffset.UTC);
                } catch (Exception e3) {
                    throw new IllegalArgumentException(
                            "Formato de fecha/hora inválido. Se esperaba ISO con offset, 'yyyy-MM-dd HH:mm:ss' o 'yyyy-MM-dd', pero se encontró: '" + trimmed + "'"
                    );
                }
            }
        }
    }

    public static OffsetDateTime getCellOffsetDateTime(Row row, int col) {
        try {
            Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell == null) return null;

            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().atOffset(ZoneOffset.UTC);
            } else if (cell.getCellType() == CellType.STRING) {
                return parseOffsetDateTime(cell.getStringCellValue().trim());
            }
            return null;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al leer OffsetDateTime en columna " + col + ": " + e.getMessage());
        }
    }

    // Métodos para Excel
    public static String getCellString(Row row, int col) {
        try {
            Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell == null) return null;

            switch (cell.getCellType()) {
                case STRING:
                    return parseString(cell.getStringCellValue());

                case NUMERIC:
                    // Manejar ISBN u otros números grandes sin notación científica
                    return BigDecimal.valueOf(cell.getNumericCellValue())
                            .toPlainString()
                            .trim();

                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());

                case FORMULA:
                    try {
                        return cell.getStringCellValue().trim();
                    } catch (IllegalStateException e) {
                        return BigDecimal.valueOf(cell.getNumericCellValue())
                                .toPlainString()
                                .trim();
                    }

                case BLANK:
                default:
                    return null;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Error al leer valor String en columna " + col + ": " + e.getMessage(),
                    e
            );
        }
    }


    public static Integer getCellInt(Row row, int col) {
        try {
            Cell cell = row.getCell(col);
            if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                return (int) cell.getNumericCellValue();
            } else if (cell != null && cell.getCellType() == CellType.STRING) {
                return parseInt(cell.getStringCellValue());
            }
            return null;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al leer número entero en columna " + col + ": " + e.getMessage());
        }
    }

    public static Long getCellLong(Row row, int col) {
        try {
            Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell == null) return null;

            if (cell.getCellType() == CellType.NUMERIC) {
                return (long) cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                return parseLong(cell.getStringCellValue());
            }

            return null;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al leer valor Long en columna " + col + ": " + e.getMessage());
        }
    }

    public static BigDecimal getCellBigDecimal(Row row, int col) {
        try {
            Cell cell = row.getCell(col);
            if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                return BigDecimal.valueOf(cell.getNumericCellValue());
            } else if (cell != null && cell.getCellType() == CellType.STRING) {
                return parseBigDecimal(cell.getStringCellValue());
            }
            return null;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al leer valor decimal en columna " + col + ": " + e.getMessage());
        }
    }

    public static LocalDateTime getCellDateTime(Row row, int col) {
        try {
            Cell cell = row.getCell(col);
            if (cell != null && cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue();
            } else if (cell != null && cell.getCellType() == CellType.STRING) {
                return parseDateTime(cell.getStringCellValue().trim());
            }
            return null;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al leer fecha y hora en columna " + col + ": " + e.getMessage());
        }
    }
}
