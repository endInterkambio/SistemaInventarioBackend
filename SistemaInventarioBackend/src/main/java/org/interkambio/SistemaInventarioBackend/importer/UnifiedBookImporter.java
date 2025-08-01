package org.interkambio.SistemaInventarioBackend.importer;

import lombok.RequiredArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.BookDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UnifiedBookImporter {

    private final BookExcelImporter excelImporter;
    private final BookCsvImporter csvImporter;

    public List<BookDTO> parse(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        if (filename == null) throw new IllegalArgumentException("El archivo no tiene nombre.");

        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();

        return switch (extension) {
            case "xlsx", "xls" -> excelImporter.parse(file);
            case "csv" -> csvImporter.parse(file);
            default -> throw new IllegalArgumentException("Formato de archivo no soportado: " + extension);
        };
    }
}
