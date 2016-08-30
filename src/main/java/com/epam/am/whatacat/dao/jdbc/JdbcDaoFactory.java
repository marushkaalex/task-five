package com.epam.am.whatacat.dao.jdbc;

import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.dao.DaoFactory;
import com.epam.am.whatacat.dao.UserDao;
import com.epam.am.whatacat.db.ConnectionPool;
import com.epam.am.whatacat.db.ConnectionPoolException;

import java.sql.Connection;

public class JdbcDaoFactory extends DaoFactory {
    private Connection connection;

    public JdbcDaoFactory() throws DaoException {
        try {
            this.connection = ConnectionPool.getInstance().getConnection();
        } catch (ConnectionPoolException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void release() {
        ConnectionPool.getInstance().releaseConnection(connection);
    }

    @Override
    public UserDao getUserDao() {
        return new JdbcUserDao(connection);
    }
}
