package org.interkambio.SistemaInventarioBackend.importer;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.interkambio.SistemaInventarioBackend.DTO.inventory.BookDTO;
import org.interkambio.SistemaInventarioBackend.DTO.inventory.BookStockLocationDTO;
import org.interkambio.SistemaInventarioBackend.DTO.common.SimpleIdNameDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

import static org.interkambio.SistemaInventarioBackend.importer.util.BookFieldParser.*;

@Component
@RequiredArgsConstructor
public class BookExcelImporter implements BookFileImporter {

    @Override
    public List<BookDTO> parse(MultipartFile file) throws Exception {
        List<BookDTO> books = new ArrayList<>();

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (!rowIterator.hasNext()) return books; // Excel vacío

            // -----------------------------
            // Crear mapa dinámico columna -> índice
            // -----------------------------
            Row headerRow = rowIterator.next();
            Map<String, Integer> columnIndex = new HashMap<>();
            for (Cell cell : headerRow) {
                columnIndex.put(cell.getStringCellValue().trim(), cell.getColumnIndex());
            }

            // SKU siempre requerido
            if (!columnIndex.containsKey("SKU")) {
                throw new IllegalArgumentException("No se encontró la columna obligatoria: SKU");
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                try {
                    BookDTO book = new BookDTO();

                    // -----------------------------
                    // Campos generales del libro
                    // -----------------------------
                    book.setSku(getCellString(row, columnIndex.get("SKU")));
                    book.setTitle(getCellString(row, columnIndex.get("Title")));
                    book.setIsbn(getCellString(row, columnIndex.get("ISBN")));
                    book.setAuthor(getCellString(row, columnIndex.get("Author")));
                    book.setPublisher(getCellString(row, columnIndex.get("Publisher")));
                    book.setDescription(getCellString(row, columnIndex.get("Description")));
                    book.setCategories(getCellList(row, columnIndex.get("Category")));

                    // Subjects como string
                    Integer idxSubjects = columnIndex.get("Subjects");
                    if (idxSubjects != null) {
                        String subjects = getCellString(row, idxSubjects);
                        book.setSubjects(subjects != null ? subjects.trim() : null);
                    }

                    book.setFormats(getCellList(row, columnIndex.get("Format")));
                    book.setLanguage(getCellString(row, columnIndex.get("Language")));
                    book.setImageUrl(getCellString(row, columnIndex.get("ImageUrl")));
                    book.setWebsiteUrl(getCellString(row, columnIndex.get("WebsiteUrl")));
                    book.setTag(getCellString(row, columnIndex.get("Tag")));
                    book.setProductSaleType(getCellString(row, columnIndex.get("ProductSaleType")));
                    book.setCoverPrice(getCellBigDecimal(row, columnIndex.get("CoverPrice")));
                    book.setPurchasePrice(getCellBigDecimal(row, columnIndex.get("PurchasePrice")));
                    book.setSellingPrice(getCellBigDecimal(row, columnIndex.get("SellingPrice")));
                    book.setFairPrice(getCellBigDecimal(row, columnIndex.get("FairPrice")));
                    book.setCreatedAt(getCellOffsetDateTime(row, columnIndex.get("CreatedAt")));
                    book.setUpdatedAt(getCellOffsetDateTime(row, columnIndex.get("UpdatedAt")));
                    book.setCreatedBy(new SimpleIdNameDTO(getCellLong(row, columnIndex.get("CreatedById")), null));
                    book.setUpdatedBy(new SimpleIdNameDTO(getCellLong(row, columnIndex.get("UpdatedById")), null));
                    book.setFilter(getCellString(row, columnIndex.get("Filter")));

                    // -----------------------------
                    // Crear DTO de ubicación (si la fila tiene datos reales)
                    // -----------------------------
                    boolean hasLocation =
                            (columnIndex.containsKey("WarehouseId") && getCellLong(row, columnIndex.get("WarehouseId")) != null) ||
                                    (columnIndex.containsKey("Stock") && getCellInt(row, columnIndex.get("Stock")) != null);

                    if (hasLocation) {
                        BookStockLocationDTO location = new BookStockLocationDTO();
                        location.setBookSku(book.getSku());

                        // Warehouse
                        Long warehouseId = getCellLong(row, columnIndex.get("WarehouseId"));
                        if (warehouseId != null) {
                            location.setWarehouse(new SimpleIdNameDTO(warehouseId, null));
                        }

                        // Bookcase
                        Integer bookcase = getCellInt(row, columnIndex.get("Bookcase"));
                        if (bookcase != null) location.setBookcase(bookcase);

                        // Floor
                        Integer floor = getCellInt(row, columnIndex.get("BookcaseFloor"));
                        if (floor != null) location.setBookcaseFloor(floor);

                        // Stock (si existe)
                        Integer stock = getCellInt(row, columnIndex.get("Stock"));
                        if (stock != null) {
                            location.setStock(stock);
                        }

                        // BookCondition y LocationType
                        location.setBookCondition(getCellString(row, columnIndex.get("BookCondition")));
                        location.setLocationType(getCellString(row, columnIndex.get("LocationType")));

                        book.setLocations(Collections.singletonList(location));
                    } else {
                        // Caso sin ubicación → catálogo bibliográfico
                        book.setLocations(Collections.emptyList());
                    }

                    books.add(book);

                } catch (Exception e) {
                    String sku = getCellString(row, columnIndex.get("SKU"));
                    throw new IllegalArgumentException("Error en fila con SKU: " + sku + " → " + e.getMessage(), e);
                }
            }
        }

        return books;
    }

    // -----------------------------
    // Helper: convertir lista separada por coma o punto y coma
    // -----------------------------
    private List<String> getCellList(Row row, Integer index) {
        if (index == null) return Collections.emptyList();
        String value = getCellString(row, index);
        if (value == null || value.isEmpty()) return Collections.emptyList();
        String[] parts = value.split("[,;]");
        return Arrays.stream(parts).map(String::trim).filter(s -> !s.isEmpty()).toList();
    }
}
