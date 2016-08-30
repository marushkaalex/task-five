package com.epam.am.whatacat.dao.jdbc;

public interface FieldGetter<T> {
    Object getField(T object);
}
