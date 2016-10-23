package com.epam.am.whatacat.dao.jdbc;

import com.epam.am.whatacat.dao.BaseDao;
import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.model.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractJdbcDao<T extends BaseModel> implements BaseDao<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractJdbcDao.class);

    private Connection connection;
    private Class<T> clazz;

    AbstractJdbcDao(Connection connection, Class<T> clazz) {
        this.connection = connection;
        this.clazz = clazz;
    }

    public abstract String getTableName(boolean isInsert);

    public abstract DataBinder<T> getDataBinder();

    @Override
    public T save(T model) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            if (model.getId() == null) {
                StringBuilder columnsSb = new StringBuilder();
                StringBuilder valuesSb = new StringBuilder();
                List<TableField> tableFields = getTableFields();
                for (TableField field : tableFields) {
                    if (!field.isUseOnSave()) continue;
                    columnsSb.append(field.getTitle()).append(',');
                    valuesSb.append("?,");
                }
                if (columnsSb.length() > 0) {
                    columnsSb.setLength(columnsSb.length() - 1);
                    valuesSb.setLength(valuesSb.length() - 1);
                }

                String query = String.format("INSERT INTO %s(%s) VALUES(%s);", getTableName(true), columnsSb, valuesSb);
                preparedStatement = connection.prepareStatement(query);

                Map<String, PropertyDescriptor> descriptorMap = getDescriptorMap();
                bindDataToStatement(preparedStatement, tableFields, descriptorMap, model);
                preparedStatement.execute();
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    model.setId(generatedKeys.getLong(1));
                }
                generatedKeys.close();

                LOG.debug("Entity of {} has been inserted. Id: {}", clazz, model.getId());

                return model;
            } else {
                StringBuilder sb = new StringBuilder();
                List<TableField> tableFields = getTableFields();
                for (TableField field : tableFields) {
                    if (!field.isUseOnSave()) continue;
                    sb.append(field.getTitle()).append("=?,");
                }
                if (sb.length() != 0) {
                    sb.setLength(sb.length() - 1); // remove last ,
                }
                String query = String.format("UPDATE %s SET %s WHERE id=?", getTableName(true), sb);
                preparedStatement = connection.prepareStatement(query);

                Map<String, PropertyDescriptor> descriptorMap = getDescriptorMap();
                int usedCount = bindDataToStatement(preparedStatement, tableFields, descriptorMap, model);
                preparedStatement.setLong(usedCount + 1, model.getId());
                preparedStatement.execute();

                LOG.debug("Entity of {} id [{}] has been updated", clazz, model.getId());

                return model;
            }
        } catch (SQLException | IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            throw new DaoException(e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new DaoException(e);
                }
            }
        }
    }

    private int bindDataToStatement(
            PreparedStatement preparedStatement,
            List<TableField> tableFields,
            Map<String, PropertyDescriptor> descriptorMap,
            T model
    ) throws SQLException, InvocationTargetException, IllegalAccessException {
        int usedCount = 0;
        for (int i = 0; i < tableFields.size(); i++) {
            TableField field = tableFields.get(i);
            if (!field.isUseOnSave()) continue;
            PropertyDescriptor descriptor = descriptorMap.get(field.getObjectFieldName());
            if (descriptor == null) continue;
            Object value = descriptor.getReadMethod().invoke(model);
            TableField.TypeConverter<?> typeConverter = field.getTypeConverter();
            if (typeConverter != null) {
                value = typeConverter.convert(value);
            }
            preparedStatement.setObject(i + 1, value);
            usedCount++;
        }

        return usedCount;
    }

    @Override
    public void delete(long id) throws DaoException {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("DELETE FROM " + getTableName(true) + " WHERE id=?");
            preparedStatement.setLong(1, id);
            preparedStatement.execute();

            LOG.debug("Entity of {} id [{}] has been deleted", clazz, id);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public T findById(Long id) throws DaoException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getSelectQueryWithFrom() + getJoin() + " WHERE " + getTableName(true) + ".id=?;");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            T model = null;
            if (resultSet.next()) {
                model = getDataBinder().bind(resultSet);
                LOG.debug("Entity of {} has been found by id [{}]", clazz, id);
            } else {
                LOG.debug("Entity of {} has not been found by id [{}]", clazz, id);
            }
            resultSet.close();
            return model;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    protected StringBuilder getSelectQueryWithFrom() {
        return getSelectQuery().append(" FROM ").append(getTableName(true));
    }

    protected StringBuilder getSelectQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        List<TableField> tableFields = getTableFields();
        for (TableField field : tableFields) {
            sb.append(field.getTable()).append('.').append(field.getTitle()).append(',');
        }
        if (!tableFields.isEmpty()) {
            sb.setLength(sb.length() - 1); // remove trailing ','
        }

        return sb;
    }

    @Override
    public List<T> getAll(long limit, long offset) throws DaoException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getSelectQueryWithFrom() + getJoin() + getOrderBy() + " LIMIT ? OFFSET ?");
            preparedStatement.setLong(1, limit);
            preparedStatement.setLong(2, offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<T> resultList = new ArrayList<>();
            while (resultSet.next()) {
                T t = getDataBinder().bind(resultSet);
                resultList.add(t);
            }
            resultSet.close();
            return resultList;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    protected String getJoin() {
        return "";
    }

    protected String getOrderBy() {
        return "";
    }

    protected abstract List<TableField> getTableFields();

    protected Connection getConnection() {
        return connection;
    }

    @Override
    public long count() throws DaoException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(1) FROM " + getTableName(true));
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            long count = resultSet.getLong(1);
            resultSet.close();
            return count;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Map<String, PropertyDescriptor> getDescriptorMap() throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        Map<String, PropertyDescriptor> descriptorMap = new HashMap<>();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            descriptorMap.put(descriptor.getName(), descriptor);
        }

        return descriptorMap;
    }
}