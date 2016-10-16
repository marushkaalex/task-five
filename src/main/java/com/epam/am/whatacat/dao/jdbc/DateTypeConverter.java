package com.epam.am.whatacat.dao.jdbc;

import java.sql.Timestamp;

public class DateTypeConverter implements TableField.TypeConverter<Timestamp> {
    @Override
    public Timestamp convert(Object o) {
        return new java.sql.Timestamp(((java.util.Date) o).getTime());
    }
}
