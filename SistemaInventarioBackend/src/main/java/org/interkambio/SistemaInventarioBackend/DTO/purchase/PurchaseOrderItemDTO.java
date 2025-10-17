package org.interkambio.SistemaInventarioBackend.DTO.purchase;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.interkambio.SistemaInventarioBackend.DTO.inventory.BookStockLocationDTO;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PurchaseOrderItemDTO {

    private Long id;
    private String bookTitle;
    private Integer quantity;
    private BigDecimal discount;
    private BigDecimal customPrice;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long bookId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String bookSku;

    private BookStockLocationDTO bookStockLocation;
    private Boolean isOfferActive;
    private BigDecimal offerPrice;
}
