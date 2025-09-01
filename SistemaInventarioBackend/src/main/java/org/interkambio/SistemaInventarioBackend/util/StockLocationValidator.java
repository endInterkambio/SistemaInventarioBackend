package org.interkambio.SistemaInventarioBackend.util;

import org.interkambio.SistemaInventarioBackend.model.BookStockLocation;
import org.interkambio.SistemaInventarioBackend.model.LocationType;
import org.interkambio.SistemaInventarioBackend.repository.BookStockLocationRepository;

public class StockLocationValidator {

    /**
     * Valida que no exista un duplicado de SHOWROOM para el mismo libro en Warehouse 1.
     * Ignora la ubicación actual si entity.getId() != null
     */
    public static void validateShowroomDuplicate(BookStockLocation entity, BookStockLocationRepository repository) {
        Long warehouseId = entity.getWarehouse().getId();

        // Solo Warehouse 1 permite SHOWROOM
        if (warehouseId == 1 && entity.getLocationType() == LocationType.SHOWROOM) {
            boolean exists = repository.existsByBookIdAndWarehouseIdAndLocationTypeAndIdNot(
                    entity.getBook().getId(),
                    warehouseId,
                    LocationType.SHOWROOM,
                    entity.getId() != null ? entity.getId() : -1
            );

            if (exists) {
                throw new RuntimeException(
                        "Ya existe un ejemplar de este libro en SHOWROOM. No se puede duplicar, aunque tenga distinta condición."
                );
            }
        }
    }
}
