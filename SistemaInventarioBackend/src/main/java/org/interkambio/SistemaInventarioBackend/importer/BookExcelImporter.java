package org.interkambio.SistemaInventarioBackend.importer;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.interkambio.SistemaInventarioBackend.DTO.BookDTO;
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

            if (!rowIterator.hasNext()) {
                return books; // Excel vacío
            }

            // Crear mapa dinámico de nombre de columna -> índice
            Row headerRow = rowIterator.next();
            Map<String, Integer> columnIndex = new HashMap<>();
            for (Cell cell : headerRow) {
                columnIndex.put(cell.getStringCellValue().trim(), cell.getColumnIndex());
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                BookDTO book = new BookDTO();

                book.setTitle(getCellString(row, columnIndex.get("Title")));
                book.setSku(getCellString(row, columnIndex.get("SKU")));
                book.setIsbn(getCellString(row, columnIndex.get("ISBN")));
                book.setAuthor(getCellString(row, columnIndex.get("Author")));
                book.setPublisher(getCellString(row, columnIndex.get("Publisher")));
                book.setStockOnHand(getCellInt(row, columnIndex.get("Stock")));
                book.setBook_condition(getCellString(row, columnIndex.get("BookCondition")));
                book.setDescription(getCellString(row, columnIndex.get("Description")));
                book.setCategory(getCellString(row, columnIndex.get("Category")));
                book.setSubjects(getCellString(row, columnIndex.get("Subjects")));
                book.setFormat(getCellString(row, columnIndex.get("Format")));
                book.setLanguage(getCellString(row, columnIndex.get("Language")));
                book.setImageUrl(getCellString(row, columnIndex.get("ImageUrl")));
                book.setWebsiteUrl(getCellString(row, columnIndex.get("WebsiteUrl")));
                book.setWarehouseId(getCellLong(row, columnIndex.get("WarehouseId")));
                book.setTag(getCellString(row, columnIndex.get("Tag")));
                book.setProductSaleType(getCellString(row, columnIndex.get("ProductSaleType")));
                book.setBookcase(getCellInt(row, columnIndex.get("Bookcase")));
                book.setBookcaseFloor(getCellInt(row, columnIndex.get("BookcaseFloor")));
                book.setCoverPrice(getCellBigDecimal(row, columnIndex.get("CoverPrice")));
                book.setPurchasePrice(getCellBigDecimal(row, columnIndex.get("PurchasePrice")));
                book.setSellingPrice(getCellBigDecimal(row, columnIndex.get("SellingPrice")));
                book.setFairPrice(getCellBigDecimal(row, columnIndex.get("FairPrice")));
                book.setCreatedAt(getCellDateTime(row, columnIndex.get("CreatedAt")));
                book.setUpdatedAt(getCellDateTime(row, columnIndex.get("UpdatedAt")));
                book.setCreatedBy(getCellLong(row, columnIndex.get("CreatedBy")));
                book.setUpdatedBy(getCellLong(row, columnIndex.get("UpdatedBy")));
                book.setFilter(getCellString(row, columnIndex.get("Filter")));

                books.add(book);
            }
        }

        return books;
    }
}
