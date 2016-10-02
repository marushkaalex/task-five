package com.epam.am.whatacat.model;

import java.util.ArrayList;
import java.util.Collection;

public class PaginatedArrayList<E> extends ArrayList<E> implements PaginatedList<E> {
    private int page;
    private int pageCount;

    public PaginatedArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    public PaginatedArrayList() {
    }

    public PaginatedArrayList(Collection<? extends E> c) {
        super(c);
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public int getPageCount() {
        return pageCount;
    }

    @Override
    public void setPageCount(int count) {
        pageCount = count;
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }
}
