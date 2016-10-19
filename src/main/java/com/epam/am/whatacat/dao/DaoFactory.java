package com.epam.am.whatacat.dao;

public abstract class DaoFactory implements AutoCloseable {
    /**
     * @return new instance of preferred implementation
     * @throws DaoException
     */
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

    /**
     * Release all used resources
     *
     * @throws DaoException
     */
    public void release() throws DaoException {

    }

    /**
     * Starts transcation
     *
     * @throws DaoException
     */
    public abstract void startTransaction() throws DaoException;

    /**
     * Commits started production
     *
     * @throws DaoException
     */
    public abstract void commitTransaction() throws DaoException;

    /**
     * Rollbacks current transaction
     *
     * @throws DaoException
     */
    public abstract void rollbackTransaction() throws DaoException;

    /**
     * @return UserDao implementation
     * @throws DaoException
     */
    public abstract UserDao getUserDao() throws DaoException;

    /**
     * @return PostDao implementation
     * @throws DaoException
     */
    public abstract PostDao getPostDao() throws DaoException;

    /**
     * @return CommentDao implementation
     * @throws DaoException
     */
    public abstract CommentDao getCommentDao() throws DaoException;
}
