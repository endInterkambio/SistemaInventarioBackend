package org.interkambio.SistemaInventarioBackend.exporter;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.interkambio.SistemaInventarioBackend.DTO.inventory.BookDTO;
import org.interkambio.SistemaInventarioBackend.DTO.inventory.BookStockLocationDTO;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.List;

@Component
public class BookExcelExporter {

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
                    .filter(s -> s.getBookSku().equals(book.getSku()))
                    .toList();

            if (locations.isEmpty()) {
                // Libro sin stock, fila vacía en stock
                Row row = sheet.createRow(rowNum++);
                fillBookRow(row, book, null);
            } else {
                // Libro con stock, una fila por cada ubicación
                for (BookStockLocationDTO loc : locations) {
                    Row row = sheet.createRow(rowNum++);
                    fillBookRow(row, book, loc);
                }
            }
        }

        // Ajustar ancho de columnas
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(outputStream);
        workbook.close();
    }

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
            // Campos vacíos si no hay stock
            row.createCell(col++).setCellValue("");
            row.createCell(col++).setCellValue(0);
            row.createCell(col++).setCellValue(0);
            row.createCell(col++).setCellValue(0);
            row.createCell(col++).setCellValue("");
            row.createCell(col++).setCellValue("");
        }
    }
}
