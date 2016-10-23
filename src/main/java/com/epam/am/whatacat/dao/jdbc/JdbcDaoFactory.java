package com.epam.am.whatacat.dao.jdbc;

import com.epam.am.whatacat.dao.*;
import com.epam.am.whatacat.db.ConnectionPool;
import com.epam.am.whatacat.db.ConnectionPoolException;
import com.sun.org.apache.xml.internal.security.utils.JDKXPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcDaoFactory extends DaoFactory {

    private static final Logger LOG = LoggerFactory.getLogger(JDKXPathFactory.class);

    private Connection connection;

    public JdbcDaoFactory() throws DaoException {
        try {
            this.connection = ConnectionPool.getInstance().getConnection();
        } catch (ConnectionPoolException e) {
            LOG.error("Cannot get connection from connection pool", e);
            throw new DaoException(e);
        }
    }

    @Override
    public void release() {
        ConnectionPool.getInstance().releaseConnection(connection);
        LOG.info("Connection {} has been released", connection);
    }

    @Override
    public void startTransaction() throws DaoException {
        try {
            connection.setAutoCommit(false);
            LOG.info("Transaction for connection {} has been started", connection);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void commitTransaction() throws DaoException {
        try {
            connection.commit();
            connection.setAutoCommit(true);
            LOG.info("Transaction for connection {} has been committed", connection);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void rollbackTransaction() throws DaoException {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
            LOG.info("Transaction for connection {} has been rolled back", connection);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public UserDao getUserDao() {
        LOG.info("UserDao has been created");
        return new JdbcUserDao(connection);
    }

    @Override
    public PostDao getPostDao() throws DaoException {
        LOG.info("PostDao has been created");
        return new JdbcPostDao(connection);
    }

    @Override
    public CommentDao getCommentDao() throws DaoException {
        LOG.info("CommentDao has been created");
        return new JdbcCommentDao(connection);
    }
}
