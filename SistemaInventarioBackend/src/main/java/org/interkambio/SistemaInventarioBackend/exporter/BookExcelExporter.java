package org.interkambio.SistemaInventarioBackend.exporter;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.interkambio.SistemaInventarioBackend.DTO.inventory.BookDTO;
import org.interkambio.SistemaInventarioBackend.DTO.inventory.BookStockLocationDTO;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BookExcelExporter {

    // ============================================================
    // 1️⃣ Exportación normal
    // ============================================================
    public static void exportUnifiedExcel(List<BookDTO> books,
                                          List<BookStockLocationDTO> stockLocations,
                                          OutputStream outputStream) throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Books & Stock");

        String[] columns = {
                "SKU", "Title", "ISBN", "Author", "Publisher", "Description", "Category", "Subjects", "Format", "Language",
                "ImageUrl", "WebsiteUrl", "CoverPrice", "PurchasePrice", "SellingPrice", "FairPrice", "Tag", "Filter",
                "ProductSaleType", "CreatedAt", "UpdatedAt", "CreatedById", "UpdatedById",
                "WarehouseId", "Bookcase", "BookcaseFloor", "Stock", "BookCondition", "LocationType"
        };

        // Encabezados
        Row header = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            header.createCell(i).setCellValue(columns[i]);
        }

        int rowNum = 1;

        for (BookDTO book : books) {
            List<BookStockLocationDTO> locations = stockLocations.stream()
                    .filter(s -> s.getBookSku() != null && s.getBookSku().equals(book.getSku()))
                    .collect(Collectors.toList());

            if (locations.isEmpty()) {
                Row row = sheet.createRow(rowNum++);
                fillBookRow(row, book, null);
            } else {
                for (BookStockLocationDTO loc : locations) {
                    Row row = sheet.createRow(rowNum++);
                    fillBookRow(row, book, loc);
                }
            }
        }

        autoSizeColumns(sheet, columns.length);
        workbook.write(outputStream);
        workbook.close();
    }

    // ============================================================
    // 2️⃣ Nueva función: Exportar solo la mejor condición y stock
    // ============================================================
    public static void exportHighestStockExcel(List<BookDTO> books,
                                               List<BookStockLocationDTO> stockLocations,
                                               OutputStream outputStream) throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Books - Highest Stock");

        String[] columns = {
                "SKU", "Title", "ISBN", "Author", "Publisher", "Description", "Category", "Subjects", "Format", "Language",
                "ImageUrl", "WebsiteUrl", "CoverPrice", "PurchasePrice", "SellingPrice", "FairPrice", "Tag", "Filter",
                "ProductSaleType", "CreatedAt", "UpdatedAt", "CreatedById", "UpdatedById",
                "WarehouseId", "Bookcase", "BookcaseFloor", "Stock", "BookCondition", "LocationType"
        };

        Row header = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            header.createCell(i).setCellValue(columns[i]);
        }

        int rowNum = 1;
        for (BookDTO book : books) {
            List<BookStockLocationDTO> locations = stockLocations.stream()
                    .filter(s -> s.getBookSku() != null && s.getBookSku().equals(book.getSku()))
                    .collect(Collectors.toList());

            BookStockLocationDTO bestLocation = selectBestStockLocation(locations);

            Row row = sheet.createRow(rowNum++);
            fillBookRow(row, book, bestLocation);
        }

        autoSizeColumns(sheet, columns.length);
        workbook.write(outputStream);
        workbook.close();
    }

    // ============================================================
    // 🧩 Lógica para seleccionar la mejor condición
    // ============================================================
    static BookStockLocationDTO selectBestStockLocation(List<BookStockLocationDTO> locations) {
        if (locations == null || locations.isEmpty()) return null;

        // Prioridad de condiciones
        List<String> conditionPriority = Arrays.asList("A", "B", "C", "D", "X", "U");

        // Filtrar solo los que tienen stock > 0
        List<BookStockLocationDTO> available = locations.stream()
                .filter(l -> l.getStock() != null && l.getStock() > 0)
                .collect(Collectors.toList());

        if (!available.isEmpty()) {
            // Ordenar: mejor condición y luego mayor stock
            available.sort((a, b) -> {
                int ca = conditionPriority.indexOf(a.getBookCondition());
                int cb = conditionPriority.indexOf(b.getBookCondition());
                if (ca == -1) ca = Integer.MAX_VALUE;
                if (cb == -1) cb = Integer.MAX_VALUE;

                // Comparar por condición primero
                int cmp = Integer.compare(ca, cb);
                if (cmp == 0) {
                    // Si la condición es igual, priorizar por mayor stock
                    return b.getStock().compareTo(a.getStock());
                }
                return cmp;
            });
            return available.get(0);
        }

        // Si todas las condiciones tienen stock 0, toma la mejor condición posible
        List<BookStockLocationDTO> sorted = new ArrayList<>(locations);
        sorted.sort((a, b) -> {
            int ca = conditionPriority.indexOf(a.getBookCondition());
            int cb = conditionPriority.indexOf(b.getBookCondition());
            if (ca == -1) ca = Integer.MAX_VALUE;
            if (cb == -1) cb = Integer.MAX_VALUE;
            return Integer.compare(ca, cb);
        });
        return sorted.get(0);
    }

    // ============================================================
    // 🧱 Reutiliza el método original para rellenar filas
    // ============================================================
    private static void fillBookRow(Row row, BookDTO book, BookStockLocationDTO loc) {
        int col = 0;
        row.createCell(col++).setCellValue(book.getSku() != null ? book.getSku() : "");
        row.createCell(col++).setCellValue(book.getTitle() != null ? book.getTitle() : "");
        row.createCell(col++).setCellValue(book.getIsbn() != null ? book.getIsbn() : "");
        row.createCell(col++).setCellValue(book.getAuthor() != null ? book.getAuthor() : "");
        row.createCell(col++).setCellValue(book.getPublisher() != null ? book.getPublisher() : "");
        row.createCell(col++).setCellValue(book.getDescription() != null ? book.getDescription() : "");
        row.createCell(col++).setCellValue(book.getCategories() != null ? String.join(", ", book.getCategories()) : "");
        row.createCell(col++).setCellValue(book.getSubjects() != null ? book.getSubjects() : "");
        row.createCell(col++).setCellValue(book.getFormats() != null ? String.join(", ", book.getFormats()) : "");
        row.createCell(col++).setCellValue(book.getLanguage() != null ? book.getLanguage() : "");
        row.createCell(col++).setCellValue(book.getImageUrl() != null ? book.getImageUrl() : "");
        row.createCell(col++).setCellValue(book.getWebsiteUrl() != null ? book.getWebsiteUrl() : "");
        row.createCell(col++).setCellValue(book.getCoverPrice() != null ? book.getCoverPrice().toString() : "");
        row.createCell(col++).setCellValue(book.getPurchasePrice() != null ? book.getPurchasePrice().toString() : "");
        row.createCell(col++).setCellValue(book.getSellingPrice() != null ? book.getSellingPrice().toString() : "");
        row.createCell(col++).setCellValue(book.getFairPrice() != null ? book.getFairPrice().toString() : "");
        row.createCell(col++).setCellValue(book.getTag() != null ? book.getTag() : "");
        row.createCell(col++).setCellValue(book.getFilter() != null ? book.getFilter() : "");
        row.createCell(col++).setCellValue(book.getProductSaleType() != null ? book.getProductSaleType() : "");
        row.createCell(col++).setCellValue(book.getCreatedAt() != null ? book.getCreatedAt().toString() : "");
        row.createCell(col++).setCellValue(book.getUpdatedAt() != null ? book.getUpdatedAt().toString() : "");
        row.createCell(col++).setCellValue(book.getCreatedBy() != null && book.getCreatedBy().getId() != null ? book.getCreatedBy().getId().toString() : "");
        row.createCell(col++).setCellValue(book.getUpdatedBy() != null && book.getUpdatedBy().getId() != null ? book.getUpdatedBy().getId().toString() : "");

        if (loc != null) {
            row.createCell(col++).setCellValue(loc.getWarehouse() != null && loc.getWarehouse().getId() != null ? loc.getWarehouse().getId().toString() : "");
            row.createCell(col++).setCellValue(loc.getBookcase() != null ? loc.getBookcase() : 0);
            row.createCell(col++).setCellValue(loc.getBookcaseFloor() != null ? loc.getBookcaseFloor() : 0);
            row.createCell(col++).setCellValue(loc.getStock() != null ? loc.getStock() : 0);
            row.createCell(col++).setCellValue(loc.getBookCondition() != null ? loc.getBookCondition() : "");
            row.createCell(col++).setCellValue(loc.getLocationType() != null ? loc.getLocationType() : "");
        } else {
            row.createCell(col++).setCellValue("");
            row.createCell(col++).setCellValue(0);
            row.createCell(col++).setCellValue(0);
            row.createCell(col++).setCellValue(0);
            row.createCell(col++).setCellValue("");
            row.createCell(col++).setCellValue("");
        }
    }

    private static void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
