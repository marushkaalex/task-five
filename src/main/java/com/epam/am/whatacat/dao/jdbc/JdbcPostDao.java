package com.epam.am.whatacat.dao.jdbc;

import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.dao.PostDao;
import com.epam.am.whatacat.dao.jdbc.binder.PostDataBinder;
import com.epam.am.whatacat.model.Gender;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.model.PostRating;
import com.epam.am.whatacat.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class JdbcPostDao extends AbstractJdbcDao<Post> implements PostDao {
    public static final String TABLE_NAME = "post";

    private DataBinder<Post> dataBinder = new PostDataBinder();

    public JdbcPostDao(Connection connection) {
        super(connection, Post.class);
    }

    @Override
    public String getTableName(boolean isInsert) {
        return "post";
    }

    @Override
    public List<Post> getAllWithUserRating(long limit, long offset, long userId) throws DaoException {
        String query = getSelectQuery() + ",post_rating.id,post_rating.post_id,post_rating.user_id,post_rating.delta,post_rating.date_ FROM post LEFT JOIN post_rating ON post.id=post_rating.post_id AND post_rating.user_id=?" + getJoin() + " LIMIT ? OFFSET ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, limit);
            preparedStatement.setLong(3, offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Post> res = new ArrayList<>();
            while (resultSet.next()) {
                Post post = bindDataWithRating(resultSet);
                res.add(post);
            }
            return res;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public DataBinder<Post> getDataBinder() {
        return dataBinder;
    }

    public Post bindDataWithRating(ResultSet resultSet) throws DaoException {
        try {
            Post post = dataBinder.bind(resultSet);

            PostRating postRating = new PostRating();
            postRating.setId(resultSet.getLong("post_rating.id"));
            postRating.setUserId(resultSet.getLong("post_rating.user_id"));
            postRating.setPostId(resultSet.getLong("post_rating.post_id"));
            postRating.setRatingDelta(resultSet.getInt("post_rating.delta"));
            java.sql.Date date = resultSet.getDate("post_rating.date_");
            if (date != null) {
                postRating.setDate(new Date(date.getTime()));
            }

            post.setUserPostRating(postRating);
            return post;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void rate(PostRating postRating) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            if (postRating.getId() == null) {
                preparedStatement = getConnection().prepareStatement("INSERT INTO post_rating(post_id, user_id, delta, date_) VALUES(?, ?, ?, ?)");
                preparedStatement.setLong(1, postRating.getPostId());
                preparedStatement.setLong(2, postRating.getUserId());
                preparedStatement.setInt(3, postRating.getRatingDelta());
                preparedStatement.setDate(4, new java.sql.Date(new Date().getTime()));
                preparedStatement.execute();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    postRating.setId(generatedKeys.getLong(1));
                }
                generatedKeys.close();
            } else {
                preparedStatement = getConnection().prepareStatement("UPDATE post_rating SET post_id=?, user_id=?, delta=?, date_=? WHERE id=?");
                preparedStatement.setLong(1, postRating.getPostId());
                preparedStatement.setLong(2, postRating.getUserId());
                preparedStatement.setInt(3, postRating.getRatingDelta());
                preparedStatement.setDate(4, new java.sql.Date(new Date().getTime()));
                preparedStatement.setLong(5, postRating.getId());

                preparedStatement.execute();
            }
        } catch (SQLException e) {
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

    @Override
    public PostRating getRating(long postId, long userId) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = getConnection().prepareStatement("SELECT id, delta, date_ FROM post_rating WHERE post_id=? AND user_id=?");
            preparedStatement.setLong(1, postId);
            preparedStatement.setLong(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            PostRating rating = null;
            if (resultSet.next()) {
                rating = new PostRating();
                rating.setId(resultSet.getLong("id"));
                rating.setPostId(postId);
                rating.setUserId(userId);
                rating.setRatingDelta(resultSet.getInt("delta"));
                java.sql.Date date_ = resultSet.getDate("date_");
                rating.setDate(new Date(date_.getTime()));
            }

            return rating;
        } catch (SQLException e) {
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

    @Override
    public long countByStatus(int status) throws DaoException {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT COUNT(1) FROM " + getTableName(false) + " WHERE " + TABLE_NAME + ".status=?");
            preparedStatement.setInt(1, status);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            long count = resultSet.getLong(1);
            return count;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Post findByIdWithRating(long postId, long userId) throws DaoException {
        String query = getSelectQuery() + ",post_rating.id,post_rating.post_id,post_rating.user_id,post_rating.delta,post_rating.date_ FROM post LEFT JOIN post_rating ON post.id=post_rating.post_id AND post_rating.user_id=? WHERE post.id=?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            Post res = new Post();
            if (resultSet.next()) {
                res = bindDataWithRating(resultSet);
            }
            return res;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected String getJoin() {
        return " JOIN user ON post.author_id=user.id JOIN role ON user.role_id=role.id";
    }

    @Override
    protected List<TableField> getTableFields() {

        return Arrays.asList(
                new TableField(TABLE_NAME, "id"),
                new TableField(TABLE_NAME, "title"),
                new TableField(TABLE_NAME, "type"),
                new TableField(TABLE_NAME, "content"),
                new TableField(TABLE_NAME, "date", "publicationDate").setTypeConverter(o -> new java.sql.Date(((Date) o).getTime())),
                new TableField(TABLE_NAME, "rating"),
                new TableField(TABLE_NAME, "author_id", "authorId"),
                new TableField(TABLE_NAME, "status"),
                new TableField("user", "id").setUseOnSave(false),
                new TableField("user", "email").setUseOnSave(false),
                new TableField("user", "nickname").setUseOnSave(false),
                new TableField("user", "password", "hashedPassword").setUseOnSave(false),
                new TableField("user", "role_id", "role").setTypeConverter(o -> ((Role) o).getId()).setUseOnSave(false),
                new TableField("user", "gender").setTypeConverter(o -> ((Gender) o).getKey()).setUseOnSave(false),
                new TableField("user", "rating").setUseOnSave(false),
                new TableField("user", "avatar", "avatarUrl").setUseOnSave(false),
                new TableField("user", "date", "registrationDate").setTypeConverter(o -> new java.sql.Date(((Date) o).getTime())).setUseOnSave(false),
                new TableField("role", "name").setUseOnSave(false).setUseOnSave(false)
        );
    }

    @Override
    public List<Post> getByStatus(int status, long limit, long offset) throws DaoException {
        String query = getSelectQueryWithFrom() + getJoin() + " WHERE " + TABLE_NAME + ".status=? LIMIT ? OFFSET ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setLong(1, status);
            preparedStatement.setLong(2, limit);
            preparedStatement.setLong(3, offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Post> res = new ArrayList<>();
            while (resultSet.next()) {
                Post post = getDataBinder().bind(resultSet);
                res.add(post);
            }
            return res;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
