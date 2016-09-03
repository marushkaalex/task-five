package com.epam.am.whatacat.dao.jdbc;

import com.epam.am.whatacat.dao.BaseDao;
import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.model.BaseModel;

import java.sql.*;
import java.util.List;
import java.util.Map;

public abstract class AbstractJdbcDao<T extends BaseModel> implements BaseDao<T> {
    private Connection connection;

    AbstractJdbcDao(Connection connection) {
        this.connection = connection;
    }

    public abstract String getTableName();

    public abstract T bindData(ResultSet resultSet);

    @Override
    public void save(T model) throws DaoException {
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
            } else {
                // TODO
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
        return null;
    }

    @Override
    public List<T> getAll(long limit, long offset) throws DaoException {

        return null;
    }

    protected abstract List<Map.Entry<String, FieldGetter<T>>> getColumns();
}