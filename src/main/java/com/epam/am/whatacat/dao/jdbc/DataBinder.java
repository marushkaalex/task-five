package com.epam.am.whatacat.dao.jdbc;

import com.epam.am.whatacat.dao.DaoException;

import java.sql.ResultSet;

public interface DataBinder<T> {
    T bind(ResultSet resultSet) throws DaoException;
}
