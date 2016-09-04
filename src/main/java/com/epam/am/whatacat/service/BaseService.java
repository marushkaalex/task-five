package com.epam.am.whatacat.service;

import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.dao.DaoFactory;

public abstract class BaseService {
    protected DaoFactory daoFactory;

    public BaseService() throws ServiceException {
        try {
            daoFactory = DaoFactory.getFactory();
        } catch (DaoException e) {
            throw new ServiceException("Unable to create service", e);
        }
    }

    public void release() throws ServiceException {
        try {
            daoFactory.release();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
