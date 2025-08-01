package org.interkambio.SistemaInventarioBackend.importer;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderHeaderAware;
import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.BookDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.interkambio.SistemaInventarioBackend.importer.util.BookFieldParser.*;

@Component
@RequiredArgsConstructor
public class BookCsvImporter implements BookFileImporter {

    @Override
    public List<BookDTO> parse(MultipartFile file) throws Exception {
        List<BookDTO> books = new ArrayList<>();

        try (var reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             var csvReader = new CSVReaderHeaderAware(reader)) {

            Map<String, String> row;
            while ((row = csvReader.readMap()) != null) {
                BookDTO book = new BookDTO();

                book.setTitle(parseString(row.get("Title")));
                book.setSku(parseString(row.get("SKU")));
                book.setIsbn(parseString(row.get("ISBN")));
                book.setAuthor(parseString(row.get("Author")));
                book.setPublisher(parseString(row.get("Publisher")));
                book.setStockOnHand(parseInt(row.get("StockOnHand")));
                book.setBook_condition(parseString(row.get("BookCondition")));
                book.setDescription(parseString(row.get("Description")));
                book.setCategory(parseString(row.get("Category")));
                book.setSubjects(parseString(row.get("Subjects")));
                book.setFormat(parseString(row.get("Format")));
                book.setLanguage(parseString(row.get("Language")));
                book.setImageUrl(parseString(row.get("ImageUrl")));
                book.setWebsiteUrl(parseString(row.get("WebsiteUrl")));
                book.setWarehouseId(parseLong(row.get("WarehouseId")));
                book.setTag(parseString(row.get("Tag")));
                book.setProductSaleType(parseString(row.get("ProductSaleType")));
                book.setBookcase(parseInt(row.get("Bookcase")));
                book.setBookcaseFloor(parseInt(row.get("BookcaseFloor")));
                book.setCoverPrice(parseBigDecimal(row.get("CoverPrice")));
                book.setPurchasePrice(parseBigDecimal(row.get("PurchasePrice")));
                book.setSellingPrice(parseBigDecimal(row.get("SellingPrice")));
                book.setFairPrice(parseBigDecimal(row.get("FairPrice")));
                book.setCreatedBy(parseLong(row.get("CreatedBy")));
                book.setUpdatedBy(parseLong(row.get("UpdatedBy")));
                book.setCreatedAt(parseDateTime(row.get("CreatedAt")));
                book.setUpdatedAt(parseDateTime(row.get("UpdatedAt")));
                book.setFilter(parseString(row.get("Filter")));

                books.add(book);
            }
        }

        return books;
    }
}
