package com.epam.am.whatacat.dao;

import java.util.List;

public interface BaseDao<T> {
    T save(T model) throws DaoException;

    void delete(long id) throws DaoException;

    T findById(Long id) throws DaoException;

    List<T> getAll(long limit, long offset) throws DaoException;

    long count() throws DaoException;
}
