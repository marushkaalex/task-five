package com.epam.am.whatacat.dao;

import java.util.List;

public interface BaseDao<T> {
    void save(T model) throws DaoException;

    void delete(T model) throws DaoException;

    T findById(Long id) throws DaoException;

    List<T> getAll(long limit, long offset) throws DaoException;
}
