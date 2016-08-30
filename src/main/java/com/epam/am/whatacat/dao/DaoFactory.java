package com.epam.am.whatacat.dao;

public abstract class DaoFactory {
    public static DaoFactory getFactory() throws DaoException {
        try {
            // TODO get class name from properties
            String className = "com.epam.am.pool.dao.jdbc.JdbcDaoFactory";
            Class<?> factoryClass = Class.forName(className);
            return ((DaoFactory) factoryClass.newInstance());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new DaoException(e);
        }
    }

    public void release() {

    }

    public abstract void startTransaction() throws DaoException;

    public abstract void commitTransaction() throws DaoException;

    public abstract void rollbackTransaction() throws DaoException;

    public abstract UserDao getUserDao() throws DaoException;
}
