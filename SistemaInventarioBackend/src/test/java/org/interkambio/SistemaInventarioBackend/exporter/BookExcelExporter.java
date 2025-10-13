package org.interkambio.SistemaInventarioBackend.exporter;

import org.interkambio.SistemaInventarioBackend.DTO.inventory.BookStockLocationDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookExcelExporterTest {

    @Test
    void testSelectBestStockLocation_PrioritizeConditionOverStock() {
        BookStockLocationDTO locA = new BookStockLocationDTO();
        locA.setBookCondition("A");
        locA.setStock(3);

        BookStockLocationDTO locB = new BookStockLocationDTO();
        locB.setBookCondition("B");
        locB.setStock(15);

        BookStockLocationDTO locC = new BookStockLocationDTO();
        locC.setBookCondition("C");
        locC.setStock(10);

        List<BookStockLocationDTO> locations = Arrays.asList(locB, locA, locC);

        BookStockLocationDTO best = BookExcelExporter.selectBestStockLocation(locations);

        assertNotNull(best);
        assertEquals("A", best.getBookCondition());
    }

    @Test
    void testSelectBestStockLocation_SameCondition_PrioritizeHigherStock() {
        BookStockLocationDTO loc1 = new BookStockLocationDTO();
        loc1.setBookCondition("B");
        loc1.setStock(8);

        BookStockLocationDTO loc2 = new BookStockLocationDTO();
        loc2.setBookCondition("B");
        loc2.setStock(15);

        List<BookStockLocationDTO> locations = Arrays.asList(loc1, loc2);

        BookStockLocationDTO best = BookExcelExporter.selectBestStockLocation(locations);

        assertNotNull(best);
        assertEquals(15, best.getStock());
    }

    @Test
    void testSelectBestStockLocation_AllZeroStock_PrioritizeCondition() {
        BookStockLocationDTO locA = new BookStockLocationDTO();
        locA.setBookCondition("A");
        locA.setStock(0);

        BookStockLocationDTO locB = new BookStockLocationDTO();
        locB.setBookCondition("B");
        locB.setStock(0);

        List<BookStockLocationDTO> locations = Arrays.asList(locB, locA);

        BookStockLocationDTO best = BookExcelExporter.selectBestStockLocation(locations);

        assertNotNull(best);
        assertEquals("A", best.getBookCondition());
    }
}
