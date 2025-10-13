package org.interkambio.SistemaInventarioBackend.importer;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.interkambio.SistemaInventarioBackend.DTO.sales.CustomerDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import static org.interkambio.SistemaInventarioBackend.importer.util.DataFieldParser.*;

import java.io.InputStream;
import java.util.*;

@Component
@RequiredArgsConstructor
public class CustomerExcelImporter implements CustomerFileImporter {

    @Override
    public List<CustomerDTO> parse(MultipartFile file) throws Exception {
        List<CustomerDTO> customers = new ArrayList<>();

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (!rowIterator.hasNext()) return customers; // Excel vacío

            // Crear mapa dinámico columna -> índice

            Row headerRow = rowIterator.next();
            Map<String, Integer> columnIndex = new HashMap<>();
            for (Cell cell : headerRow) {
                columnIndex.put(cell.getStringCellValue().trim(), cell.getColumnIndex());
            }

            // Validar columnas requeridas
            List<String> requiredColumns = Arrays.asList(
                    "CustomerType", "DocumentType", "DocumentNumber",
                    "Name", "Email", "PhoneNumber"
            );

            List<String> missingColumns = new ArrayList<>();
            for (String col : requiredColumns) {
                if (!columnIndex.containsKey(col)) {
                    missingColumns.add(col);
                }
            }

            if (!missingColumns.isEmpty()) {
                throw new IllegalArgumentException(
                        "Faltan columnas requeridas: " + String.join(", ", missingColumns)
                );
            }

            // Procesar filas
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                try {
                    CustomerDTO customer = new CustomerDTO();

                    // Campos general de cliente

                    customer.setCustomerType(getCellString(row, columnIndex.get("CustomerType")));
                    customer.setDocumentType(getCellString(row, columnIndex.get("DocumentType")));
                    customer.setDocumentNumber(getCellString(row, columnIndex.get("DocumentNumber")));
                    customer.setName(getCellString(row, columnIndex.get("Name")));
                    customer.setCompanyName(getCellString(row, columnIndex.get("CompanyName")));
                    customer.setEmail(getCellString(row, columnIndex.get("Email")));
                    customer.setPhoneNumber(getCellString(row, columnIndex.get("PhoneNumber")));
                    customer.setAddress(getCellString(row, columnIndex.get("Address")));

                    customers.add(customer);

                } catch (Exception e) {
                    throw new RuntimeException("Error en fila " + row.getRowNum() + ": " + e.getMessage(), e);
                }
            }
        }
        return customers;
    }
}
