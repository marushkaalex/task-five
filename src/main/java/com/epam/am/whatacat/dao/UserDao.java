package com.epam.am.whatacat.dao;

import com.epam.am.whatacat.model.User;

public interface UserDao extends BaseDao<User> {
    boolean isNicknameFree(String nickname) throws DaoException;

    boolean isEmailFree(String email) throws DaoException;

    User findByEmail(String email) throws DaoException;
}
