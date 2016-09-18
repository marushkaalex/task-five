package com.epam.am.whatacat.dao.jdbc;

public class TableField {
    private String table;
    private String title;
    private String objectFieldName;
    private boolean useOnSave = true;
    private TypeConverter<?> typeConverter;

    public TableField(String table, String title, String fieldName) {
        this.table = table;
        this.title = title;
        this.objectFieldName = fieldName;
    }

    public TableField(String table, String title) {
        this(table, title, title);
    }

    public String getTable() {
        return table;
    }

    public TableField setTable(String table) {
        this.table = table;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TableField setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getObjectFieldName() {
        return objectFieldName;
    }

    public TableField setObjectFieldName(String objectFieldName) {
        this.objectFieldName = objectFieldName;
        return this;
    }

    public boolean isUseOnSave() {
        return useOnSave;
    }

    public TableField setUseOnSave(boolean useOnSave) {
        this.useOnSave = useOnSave;
        return this;
    }

    public TypeConverter<?> getTypeConverter() {
        return typeConverter;
    }

    public TableField setTypeConverter(TypeConverter<?> typeConverter) {
        this.typeConverter = typeConverter;
        return this;
    }

    public interface TypeConverter<T> {
        T convert(Object o);
    }

    @Override
    public String toString() {
        return "TableField{" +
                "table='" + table + '\'' +
                ", title='" + title + '\'' +
                ", objectFieldName='" + objectFieldName + '\'' +
                '}';
    }
}
