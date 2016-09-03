package com.epam.am.whatacat.dao;

import com.epam.am.whatacat.model.User;

public interface UserDao extends BaseDao<User> {
    boolean isNicknameFree(String nickname);

    boolean isEmailFree(String email);
}
