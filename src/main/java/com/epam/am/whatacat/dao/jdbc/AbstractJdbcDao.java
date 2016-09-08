package com.epam.am.whatacat.dao.jdbc;

import com.epam.am.whatacat.dao.BaseDao;
import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.model.BaseModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractJdbcDao<T extends BaseModel> implements BaseDao<T> {
    private Connection connection;

    AbstractJdbcDao(Connection connection) {
        this.connection = connection;
    }

    public abstract String getTableName();

    public abstract T bindData(ResultSet resultSet) throws DaoException;

    @Override
    public T save(T model) throws DaoException {
        try {
            if (model.getId() == null) {
                // TODO
                StringBuilder columnsSb = new StringBuilder();
                StringBuilder valuesSb = new StringBuilder();
                List<Map.Entry<String, FieldGetter<T>>> columns = getColumns();
                for (Map.Entry<String, FieldGetter<T>> entry : columns) {
                    columnsSb.append(entry.getKey()).append(',');
                    valuesSb.append("?,");
                }
                if (columnsSb.length() > 0) {
                    columnsSb.setLength(columnsSb.length() - 1);
                    valuesSb.setLength(valuesSb.length() - 1);
                }

                String query = String.format("INSERT INTO %s(%s) VALUES(%s);", getTableName(), columnsSb, valuesSb);
                System.out.println(query);
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                for (int i = 0; i < columns.size(); i++) {
                    Map.Entry<String, FieldGetter<T>> entry = columns.get(i);
                    preparedStatement.setObject(i + 1, entry.getValue().getField(model));
                }
                preparedStatement.execute();
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    model.setId(generatedKeys.getLong(1));
                }
                generatedKeys.close();
                return model;
            } else {
                // TODO
                throw new UnsupportedOperationException("User update is not currently implemented");
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(T model) throws DaoException {

    }

    @Override
    public T findById(Long id) throws DaoException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getSelectQuery() + " WHERE id=?;");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return bindData(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    protected String getSelectQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        List<Map.Entry<String, FieldGetter<T>>> columns = getColumns();
        for (Map.Entry<String, FieldGetter<T>> entry : columns) {
            sb.append(entry.getKey()).append(',');
        }
        if (!columns.isEmpty()) {
            sb.setLength(sb.length() - 1); // remove trailing ','
        }
        sb.append(" FROM ").append(getTableName());

        return sb.toString();
    }

    @Override
    public List<T> getAll(long limit, long offset) throws DaoException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getSelectQuery() + " LIMIT ? OFFSET ?");
            preparedStatement.setLong(1, limit);
            preparedStatement.setLong(2, offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<T> resultList = new ArrayList<>();
            while (resultSet.next()) {
                T t = bindData(resultSet);
                resultList.add(t);
            }
            resultSet.close();
            return resultList;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    protected abstract List<Map.Entry<String, FieldGetter<T>>> getColumns();

    protected Connection getConnection() {
        return connection;
    }
}