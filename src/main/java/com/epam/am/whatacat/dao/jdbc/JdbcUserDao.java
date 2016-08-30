package com.epam.am.whatacat.dao.jdbc;

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
        return null;
    }

    @Override
    protected List<Map.Entry<String, FieldGetter<User>>> getColumns() {
        return columnMap;
    }
}
