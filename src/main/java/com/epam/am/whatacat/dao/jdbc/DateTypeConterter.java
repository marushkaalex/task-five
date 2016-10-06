package com.epam.am.whatacat.dao.jdbc;

import java.sql.Date;

public class DateTypeConterter implements TableField.TypeConverter<Date> {
    @Override
    public Date convert(Object o) {
        return new java.sql.Date(((java.util.Date) o).getTime());
    }
}
