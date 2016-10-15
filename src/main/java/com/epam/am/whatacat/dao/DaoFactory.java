package com.epam.am.whatacat.dao;

public abstract class DaoFactory implements AutoCloseable {
    public static DaoFactory getFactory() throws DaoException {
        try {
            // TODO base class name from properties
            String className = "com.epam.am.whatacat.dao.jdbc.JdbcDaoFactory";
            Class<?> factoryClass = Class.forName(className);
            return ((DaoFactory) factoryClass.newInstance());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void close() throws DaoException {
        release();
    }

    public void release() throws DaoException {

    }

    public abstract void startTransaction() throws DaoException;

    public abstract void commitTransaction() throws DaoException;

    public abstract void rollbackTransaction() throws DaoException;

    public abstract UserDao getUserDao() throws DaoException;

    public abstract PostDao getPostDao() throws DaoException;

    public abstract CommentDao getCommentDao() throws DaoException;
}
