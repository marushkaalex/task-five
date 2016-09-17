package com.epam.am.whatacat.dao.jdbc;

import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.dao.UserDao;
import com.epam.am.whatacat.model.BaseModel;
import com.epam.am.whatacat.model.Gender;
import com.epam.am.whatacat.model.Role;
import com.epam.am.whatacat.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcUserDao extends AbstractJdbcDao<User> implements UserDao {
    private static final List<Map.Entry<String, FieldGetter<User>>> columnMap = new ArrayList<>();

    static {
        columnMap.add(new AbstractMap.SimpleEntry<>("user.id", BaseModel::getId));
        columnMap.add(new AbstractMap.SimpleEntry<>("user.email", User::getEmail));
        columnMap.add(new AbstractMap.SimpleEntry<>("user.nickname", User::getNickname));
        columnMap.add(new AbstractMap.SimpleEntry<>("user.password", User::getHashedPassword));
        columnMap.add(new AbstractMap.SimpleEntry<>("user.role_id", i -> i.getRole().getId()));
        columnMap.add(new AbstractMap.SimpleEntry<>("user.gender", i -> i.getGender().getKey()));
        columnMap.add(new AbstractMap.SimpleEntry<>("user.rating", User::getRating));
        columnMap.add(new AbstractMap.SimpleEntry<>("user.avatar", User::getAvatarUrl));
        columnMap.add(new AbstractMap.SimpleEntry<>("user.date", i -> new java.sql.Date(i.getRegistrationDate().getTime())));
        columnMap.add(new AbstractMap.SimpleEntry<>("role.name", null));
    }

    public JdbcUserDao(Connection connection) {
        super(connection);
    }

    @Override
    public String getTableName() {
        return "user, role";
    }

    @Override
    public User bindData(ResultSet resultSet) throws DaoException {
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
            return user;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected List<Map.Entry<String, FieldGetter<User>>> getColumns() {
        return columnMap;
    }

    @Override
    public List<User> getAll(long limit, long offset) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isNicknameFree(String nickname) throws DaoException {
        try {
            PreparedStatement preparedStatement =
                    getConnection().prepareStatement(getSelectQueryWithFrom() + " WHERE nickname=?");

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
                    getConnection().prepareStatement(getSelectQueryWithFrom() + " WHERE email=?");

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
                    getConnection().prepareStatement(getSelectQueryWithFrom() + " WHERE email=? AND password=?");

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = bindData(resultSet);
            }
            resultSet.close();
            return user;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
