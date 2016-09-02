package com.epam.am.whatacat.model;

import java.util.List;

public interface PaginatedList<E> extends List<E> {
    int getPage();

    void setPage(int pageNumber);
}
