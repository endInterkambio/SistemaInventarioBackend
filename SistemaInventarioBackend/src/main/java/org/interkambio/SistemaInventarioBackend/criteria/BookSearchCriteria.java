package org.interkambio.SistemaInventarioBackend.criteria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookSearchCriteria {
    private String search;

    private String title;
    private String author;
    private String isbn;
    private String sku;
    private String publisher;
    private String category;
    private String subjects;
    private String format;
    private String tag;
    private String filter;

    private Double minPrice;
    private Double maxPrice;

    private Integer minStock;
    private Integer maxStock;

    private Integer shelf;
    private Integer floor;

    private String sortBy;
    private String sortDirection;
}
