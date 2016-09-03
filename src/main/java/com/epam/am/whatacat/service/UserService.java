package com.epam.am.whatacat.service;

import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.dao.DaoFactory;
import com.epam.am.whatacat.dao.UserDao;
import com.epam.am.whatacat.model.User;

public class UserService {
    DaoFactory daoFactory;

    public UserService() throws ServiceException {
        try {
            this.daoFactory = DaoFactory.getFactory();
        } catch (DaoException e) {
            throw new ServiceException("Unable to create service", e);
        }
    }

    public void register(User user) throws ServiceException {
        try {
            UserDao userDao = daoFactory.getUserDao();
            userDao.save(user);
        } catch (DaoException e) {
            throw new ServiceException("Unable to register user", e);
        }

    }
}
