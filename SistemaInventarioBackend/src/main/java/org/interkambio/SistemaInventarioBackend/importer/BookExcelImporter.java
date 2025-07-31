package org.interkambio.SistemaInventarioBackend.importer;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.interkambio.SistemaInventarioBackend.DTO.BookDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookExcelImporter {

    public List<BookDTO> parse(MultipartFile file) throws Exception {
        List<BookDTO> books = new ArrayList<>();

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next(); // Saltar encabezado
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                BookDTO book = new BookDTO();

                book.setTitle(getString(row, 0));
                book.setSku(getString(row, 1));
                book.setIsbn(getString(row, 2));
                book.setAuthor(getString(row, 3));
                book.setPublisher(getString(row, 4));
                book.setStockOnHand(getInt(row, 5));
                book.setBook_condition(getString(row, 6));
                book.setDescription(getString(row, 7));
                book.setCategory(getString(row, 8));
                book.setSubjects(getString(row, 9));
                book.setFormat(getString(row, 10));
                book.setLanguage(getString(row, 11));
                book.setImageUrl(getString(row, 12));
                book.setWebsiteUrl(getString(row, 13));
                book.setTag(getString(row, 14));
                book.setProductSaleType(getString(row, 15));
                book.setBookcase(getInt(row, 16));
                book.setBookcaseFloor(getInt(row, 17));
                book.setCoverPrice(getBigDecimal(row, 18));
                book.setPurchasePrice(getBigDecimal(row, 19));
                book.setSellingPrice(getBigDecimal(row, 20));
                book.setFairPrice(getBigDecimal(row, 21));
                book.setCreatedBy(getLong(row, 22));
                book.setUpdatedBy(getLong(row, 23));

                System.out.println("Row " + row.getRowNum() + " -> updatedBy: " + book.getUpdatedBy());

                book.setCreatedAt(getDateTime(row, 24));
                book.setUpdatedAt(getDateTime(row, 25));
                book.setFilter(getString(row, 26));

                books.add(book);
            }
        }

        return books;
    }

    private String getString(Row row, int col) {
        try {
            Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            return (cell != null) ? cell.toString().trim() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private Integer getInt(Row row, int col) {
        try {
            Cell cell = row.getCell(col);
            return (cell != null) ? (int) cell.getNumericCellValue() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private Long getLong(Row row, int col) {
        try {
            Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell == null) return null;

            if (cell.getCellType() == CellType.NUMERIC) {
                long value = (long) cell.getNumericCellValue();
                return value > 0 ? value : null;
            } else if (cell.getCellType() == CellType.STRING) {
                String stringValue = cell.getStringCellValue().trim();
                if (stringValue.isEmpty()) return null;
                long value = Long.parseLong(stringValue);
                return value > 0 ? value : null;
            }

        } catch (Exception e) {
            // Logueo opcional
        }
        return null;
    }

    private BigDecimal getBigDecimal(Row row, int col) {
        try {
            Cell cell = row.getCell(col);
            return (cell != null) ? BigDecimal.valueOf(cell.getNumericCellValue()) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDateTime getDateTime(Row row, int col) {
        try {
            Cell cell = row.getCell(col);
            if (cell != null && cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue();
            } else if (cell != null && cell.getCellType() == CellType.STRING) {
                return LocalDateTime.parse(cell.getStringCellValue().trim());
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
