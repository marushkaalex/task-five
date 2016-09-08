package com.epam.am.whatacat.dao.jdbc;

import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.dao.PostDao;
import com.epam.am.whatacat.model.Post;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcPostDao extends AbstractJdbcDao<Post> implements PostDao {
    private static final List<Map.Entry<String, FieldGetter<Post>>> columnList = new ArrayList<>();

    static {
        columnList.add(new AbstractMap.SimpleEntry<>("id", Post::getId));
        columnList.add(new AbstractMap.SimpleEntry<>("title", Post::getTitle));
        columnList.add(new AbstractMap.SimpleEntry<>("type", Post::getType));
        columnList.add(new AbstractMap.SimpleEntry<>("content", Post::getContent));
        columnList.add(new AbstractMap.SimpleEntry<>("date", post -> new java.sql.Date(post.getPublicationDate().getTime())));
        columnList.add(new AbstractMap.SimpleEntry<>("rating", Post::getRating));
        columnList.add(new AbstractMap.SimpleEntry<>("author_id", Post::getAuthorId));
    }

    public JdbcPostDao(Connection connection) {
        super(connection);
    }

    @Override
    public String getTableName() {
        return "post";
    }

    @Override
    public Post bindData(ResultSet resultSet) throws DaoException {
        try {
            Post res = new Post();
            res.setId(resultSet.getLong("id"));
            res.setTitle(resultSet.getString("title"));
            res.setContent(resultSet.getString("content"));
            res.setPublicationDate(new Date(resultSet.getDate("date").getTime()));
            res.setType(resultSet.getInt("type"));
            res.setRating(resultSet.getLong("rating"));
            res.setAuthorId(resultSet.getLong("author_id"));
            return res;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected List<Map.Entry<String, FieldGetter<Post>>> getColumns() {
        return columnList;
    }
}
