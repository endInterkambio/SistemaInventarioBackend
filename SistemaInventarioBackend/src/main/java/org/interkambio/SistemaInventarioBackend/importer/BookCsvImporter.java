package org.interkambio.SistemaInventarioBackend.importer;

import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.BookDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookCsvImporter {

    public List<BookDTO> parse(MultipartFile file) throws Exception {
        List<BookDTO> books = new ArrayList<>();

        try (var reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             var csvReader = new CSVReader(reader)) {

            List<String[]> records = csvReader.readAll();

            for (int i = 1; i < records.size(); i++) { // Saltar encabezado
                String[] row = records.get(i);
                BookDTO book = new BookDTO();

                book.setTitle(row[0]);
                book.setSku(row[1]);
                book.setIsbn(row[2]);
                book.setAuthor(row[3]);
                book.setPublisher(row[4]);
                book.setStockOnHand(parseInt(row[5]));
                book.setBook_condition(row[6]);
                book.setDescription(row[7]);
                book.setCategory(row[8]);
                book.setSubjects(row[9]);
                book.setFormat(row[10]);
                book.setLanguage(row[11]);
                book.setImageUrl(row[12]);
                book.setWebsiteUrl(row[13]);
                book.setTag(row[14]);
                book.setProductSaleType(row[15]);
                book.setBookcase(parseInt(row[16]));
                book.setBookcaseFloor(parseInt(row[17]));
                book.setCoverPrice(parseBigDecimal(row[18]));
                book.setPurchasePrice(parseBigDecimal(row[19]));
                book.setSellingPrice(parseBigDecimal(row[20]));
                book.setFairPrice(parseBigDecimal(row[21]));
                book.setCreatedBy(parseLong(row[22]));
                book.setUpdatedBy(parseLong(row[23]));
                book.setCreatedAt(parseDateTime(row[24]));
                book.setUpdatedAt(parseDateTime(row[25]));
                book.setFilter(row[26]);

                books.add(book);
            }
        }

        return books;
    }

    private Integer parseInt(String value) {
        try {
            return (value != null && !value.trim().isEmpty()) ? Integer.parseInt(value.trim()) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal parseBigDecimal(String value) {
        try {
            return (value != null && !value.trim().isEmpty()) ? new BigDecimal(value.trim()) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Long parseLong(String value) {
        try {
            return (value != null && !value.trim().isEmpty()) ? Long.parseLong(value.trim()) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private LocalDateTime parseDateTime(String value) {
        try {
            return (value != null && !value.trim().isEmpty()) ? LocalDateTime.parse(value.trim()) : null;
        } catch (Exception e) {
            return null;
        }
    }
}
