package com.epam.am.whatacat.dao.jdbc;

import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.dao.UserDao;
import com.epam.am.whatacat.dao.jdbc.binder.UserDataBinder;
import com.epam.am.whatacat.model.BaseModel;
import com.epam.am.whatacat.model.Gender;
import com.epam.am.whatacat.model.Role;
import com.epam.am.whatacat.model.User;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class JdbcUserDao extends AbstractJdbcDao<User> implements UserDao {
    public static final String TABLE_NAME = "user";

    private DataBinder<User> dataBinder = new UserDataBinder();

    public JdbcUserDao(Connection connection) {
        super(connection, User.class);
    }

    @Override
    public String getTableName(boolean isInsert) {
        return isInsert ? "user" : "user, role";
    }

    @Override
    public DataBinder<User> getDataBinder() {
        return dataBinder;
    }

    @Override
    public boolean isNicknameFree(String nickname) throws DaoException {
        try {
            PreparedStatement preparedStatement =
                    getConnection().prepareStatement(getSelectQueryWithFrom() + getJoin() + " WHERE nickname=?");

            preparedStatement.setString(1, nickname);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean res = resultSet.next();
            resultSet.close();
            return !res;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean isEmailFree(String email) throws DaoException {
        try {
            PreparedStatement preparedStatement =
                    getConnection().prepareStatement(getSelectQueryWithFrom() + getJoin() + " WHERE email=?");

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean res = resultSet.next();
            resultSet.close();
            return !res;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws DaoException{
        try {
            PreparedStatement preparedStatement =
                    getConnection().prepareStatement(getSelectQueryWithFrom() + getJoin() + " WHERE email=? AND password=?");

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = dataBinder.bind(resultSet);
            }
            resultSet.close();
            return user;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected List<TableField> getTableFields() {
        return Arrays.asList(
                new TableField(TABLE_NAME, "id"),
                new TableField(TABLE_NAME, "email"),
                new TableField(TABLE_NAME, "nickname"),
                new TableField(TABLE_NAME, "password", "hashedPassword"),
                new TableField(TABLE_NAME, "role_id", "role").setTypeConverter(o -> ((Role) o).getId()),
                new TableField(TABLE_NAME, "gender").setTypeConverter(o -> ((Gender) o).getKey()),
                new TableField(TABLE_NAME, "rating"),
                new TableField(TABLE_NAME, "avatar", "avatarUrl"),
                new TableField(TABLE_NAME, "date", "registrationDate").setTypeConverter(new DateTypeConterter()),
                new TableField("role", "name").setUseOnSave(false)
        );
    }

    @Override
    protected String getJoin() {
        return " JOIN role ON user.role_id=role.id";
    }
}
