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
    private static final String COLUMN_NICKNAME = "nickname";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE_ID = "role_id";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_AVATAR = "avatar";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_ID = "id";

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
        return !isFoundWhere(COLUMN_NICKNAME, nickname);
    }

    @Override
    public boolean isEmailFree(String email) throws DaoException {
        return !isFoundWhere(COLUMN_EMAIL, email);
    }

    private boolean isFoundWhere(String columnName, String value) throws DaoException {
        try (
                PreparedStatement preparedStatement =
                        getConnection().prepareStatement(getSelectQueryWithFrom() + getJoin() + " WHERE " + columnName + "=?")
        ) {
            preparedStatement.setString(1, value);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean res = resultSet.next();
            resultSet.close();
            return res;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public User findByEmail(String email) throws DaoException {
        try (
                PreparedStatement preparedStatement =
                        getConnection().prepareStatement(getSelectQueryWithFrom() + getJoin() + " WHERE " + COLUMN_EMAIL + "=?")
        ) {
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
                new TableField(TABLE_NAME_INSERT, COLUMN_ID),
                new TableField(TABLE_NAME_INSERT, COLUMN_EMAIL),
                new TableField(TABLE_NAME_INSERT, COLUMN_NICKNAME),
                new TableField(TABLE_NAME_INSERT, COLUMN_PASSWORD, "hashedPassword"),
                new TableField(TABLE_NAME_INSERT, COLUMN_ROLE_ID, "role").setTypeConverter(o -> ((Role) o).getId()),
                new TableField(TABLE_NAME_INSERT, COLUMN_GENDER).setTypeConverter(o -> ((Gender) o).getKey()),
                new TableField(TABLE_NAME_INSERT, COLUMN_RATING),
                new TableField(TABLE_NAME_INSERT, COLUMN_AVATAR, "avatarUrl"),
                new TableField(TABLE_NAME_INSERT, COLUMN_DATE, "registrationDate").setTypeConverter(new DateTypeConverter()),
                new TableField(TABLE_NAME_ROLE, COLUMN_NAME).setUseOnSave(false)
        );
    }

    @Override
    protected String getJoin() {
        return " JOIN " + TABLE_NAME_ROLE + " ON " + makeTableField(TABLE_NAME_INSERT, COLUMN_ROLE_ID) + "=" + makeTableField(TABLE_NAME_ROLE, COLUMN_ID);
    }
}
