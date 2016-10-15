package com.epam.am.whatacat.model;

import java.util.ArrayList;
import java.util.List;

public class AdminTable {
    private String title;
    private List<Row> rows;
    private Row headers;
    private int page;
    private int pageCount;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public Row getHeaders() {
        return headers;
    }

    public void setHeaders(Row headers) {
        this.headers = headers;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public static class Row {
        private List<Column> columns = new ArrayList<>();

        public List<Column> getColumns() {
            return columns;
        }

        public Row addColumn(Column column) {
            columns.add(column);
            return this;
        }

        public Row addColumn(String text) {
            columns.add(new Column(text));
            return this;
        }

        public Row addColumn(String text, String url) {
            columns.add(new Column(text, url));
            return this;
        }

        public Row addColumn(String text, boolean isKey) {
            columns.add(new Column(text, isKey));
            return this;
        }

        public Row addColumn(String text, boolean isKey, String url) {
            columns.add(new Column(text, isKey, url));
            return this;
        }
    }

    public static class Column {
        private String text;
        private boolean isKey;
        private String url;

        public Column(String text) {
            this.text = text;
        }

        public Column(String text, String url) {
            this.text = text;
            this.url = url;
        }

        public Column(String text, boolean isKey) {
            this.text = text;
            this.isKey = isKey;
        }

        public Column(String text, boolean isKey, String url) {
            this.text = text;
            this.isKey = isKey;
            this.url = url;
        }

        public String getText() {
            return text;
        }

        public String getUrl() {
            return url;
        }

        public boolean isKey() {
            return isKey;
        }
    }
}
