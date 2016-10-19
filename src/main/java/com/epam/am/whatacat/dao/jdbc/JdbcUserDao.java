package com.epam.am.whatacat.dao.jdbc;

import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.dao.UserDao;
import com.epam.am.whatacat.dao.jdbc.binder.UserDataBinder;
import com.epam.am.whatacat.model.Gender;
import com.epam.am.whatacat.model.Role;
import com.epam.am.whatacat.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class JdbcUserDao extends AbstractJdbcDao<User> implements UserDao {

    private static final String TABLE_NAME_INSERT = "user";
    private static final String TABLE_NAME = "user, role";
    private static final String TABLE_NAME_ROLE = "role";

    private DataBinder<User> dataBinder = new UserDataBinder();

    public JdbcUserDao(Connection connection) {
        super(connection, User.class);
    }

    @Override
    public String getTableName(boolean isInsert) {
        return isInsert ? TABLE_NAME_INSERT : TABLE_NAME;
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
    public User findByEmail(String email) throws DaoException {
        try {
            PreparedStatement preparedStatement =
                    getConnection().prepareStatement(getSelectQueryWithFrom() + getJoin() + " WHERE email=?");

            preparedStatement.setString(1, email);
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
                new TableField(TABLE_NAME_INSERT, "id"),
                new TableField(TABLE_NAME_INSERT, "email"),
                new TableField(TABLE_NAME_INSERT, "nickname"),
                new TableField(TABLE_NAME_INSERT, "password", "hashedPassword"),
                new TableField(TABLE_NAME_INSERT, "role_id", "role").setTypeConverter(o -> ((Role) o).getId()),
                new TableField(TABLE_NAME_INSERT, "gender").setTypeConverter(o -> ((Gender) o).getKey()),
                new TableField(TABLE_NAME_INSERT, "rating"),
                new TableField(TABLE_NAME_INSERT, "avatar", "avatarUrl"),
                new TableField(TABLE_NAME_INSERT, "date", "registrationDate").setTypeConverter(new DateTypeConverter()),
                new TableField(TABLE_NAME_ROLE, "name").setUseOnSave(false)
        );
    }

    @Override
    protected String getJoin() {
        return " JOIN role ON user.role_id=role.id";
    }
}
