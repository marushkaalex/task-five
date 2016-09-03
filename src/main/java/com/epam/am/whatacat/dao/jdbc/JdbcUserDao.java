package com.epam.am.whatacat.dao.jdbc;

import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.dao.UserDao;
import com.epam.am.whatacat.model.BaseModel;
import com.epam.am.whatacat.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;

public class JdbcUserDao extends AbstractJdbcDao<User> implements UserDao {
    private static final List<Map.Entry<String, FieldGetter<User>>> columnMap = new ArrayList<>();

    static {
        columnMap.add(new AbstractMap.SimpleEntry<>("id", BaseModel::getId));
        columnMap.add(new AbstractMap.SimpleEntry<>("email", User::getEmail));
        columnMap.add(new AbstractMap.SimpleEntry<>("nickname", User::getNickname));
        columnMap.add(new AbstractMap.SimpleEntry<>("password", User::getHashedPassword));
        columnMap.add(new AbstractMap.SimpleEntry<>("role_id", i -> i.getRole().getId()));
        columnMap.add(new AbstractMap.SimpleEntry<>("gender", i -> i.getGender().getKey()));
        columnMap.add(new AbstractMap.SimpleEntry<>("rating", User::getRating));
        columnMap.add(new AbstractMap.SimpleEntry<>("avatar", User::getAvatarUrl));
        columnMap.add(new AbstractMap.SimpleEntry<>("date", i -> new java.sql.Date(i.getRegistrationDate().getTime())));
    }

    public JdbcUserDao(Connection connection) {
        super(connection);
    }

    @Override
    public String getTableName() {
        return "user";
    }

    @Override
    public User bindData(ResultSet resultSet) {
        throw new UnsupportedOperationException();
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
    public boolean isNicknameFree(String nickname) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmailFree(String email) {
        throw new UnsupportedOperationException();
    }
}
