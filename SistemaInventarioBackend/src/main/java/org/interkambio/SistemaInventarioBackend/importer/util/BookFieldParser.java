package org.interkambio.SistemaInventarioBackend.importer.util;

import org.apache.poi.ss.usermodel.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookFieldParser {

    public static String parseString(String value) {
        return (value != null && !value.trim().isEmpty()) ? value.trim() : null;
    }

    public static Integer parseInt(String value) {
        try {
            return parseString(value) != null ? Integer.parseInt(value.trim()) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Long parseLong(String value) {
        try {
            return parseString(value) != null ? Long.parseLong(value.trim()) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static BigDecimal parseBigDecimal(String value) {
        try {
            return parseString(value) != null ? new BigDecimal(value.trim()) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static LocalDateTime parseDateTime(String value) {
        try {
            return parseString(value) != null ? LocalDateTime.parse(value.trim()) : null;
        } catch (Exception e) {
            return null;
        }
    }

    // Métodos para Excel
    public static String getCellString(Row row, int col) {
        try {
            Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            return (cell != null) ? cell.toString().trim() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer getCellInt(Row row, int col) {
        try {
            Cell cell = row.getCell(col);
            return (cell != null) ? (int) cell.getNumericCellValue() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public static Long getCellLong(Row row, int col) {
        try {
            Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell == null) return null;

            if (cell.getCellType() == CellType.NUMERIC) {
                long value = (long) cell.getNumericCellValue();
                return value > 0 ? value : null;
            } else if (cell.getCellType() == CellType.STRING) {
                return parseLong(cell.getStringCellValue());
            }

        } catch (Exception e) {
        }
        return null;
    }

    public static BigDecimal getCellBigDecimal(Row row, int col) {
        try {
            Cell cell = row.getCell(col);
            return (cell != null) ? BigDecimal.valueOf(cell.getNumericCellValue()) : null;
        } catch (Exception e) {
            return null;
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
        } catch (Exception e) {
        }
        return null;
    }
}
