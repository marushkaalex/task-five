package com.epam.am.whatacat.dao.jdbc.binder;

import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.dao.jdbc.DataBinder;
import com.epam.am.whatacat.model.Gender;
import com.epam.am.whatacat.model.Role;
import com.epam.am.whatacat.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class UserDataBinder implements DataBinder<User> {
    @Override
    public User bind(ResultSet resultSet) throws DaoException {
        try {
            User user = new User();
            user.setId(resultSet.getLong("user.id"));
            user.setEmail(resultSet.getString("user.email"));
            user.setNickname(resultSet.getString("user.nickname"));
            user.setRole(Role.valueOf(resultSet.getString("role.name")));
            user.setGender(Gender.of(resultSet.getString("user.gender").charAt(0)));
            user.setRating(resultSet.getLong("user.rating"));
            user.setAvatarUrl(resultSet.getString("user.avatar"));
            user.setRegistrationDate(new Date(resultSet.getDate("user.date").getTime()));
            user.setHashedPassword(resultSet.getString("password"));
            return user;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
