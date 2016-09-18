package com.epam.am.whatacat.dao.jdbc;

import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.dao.PostDao;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.model.PostRating;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class JdbcPostDao extends AbstractJdbcDao<Post> implements PostDao {
    public static final String TABLE_NAME = "post";

    public JdbcPostDao(Connection connection) {
        super(connection, Post.class);
    }

    @Override
    public String getTableName(boolean isInsert) {
        return "post";
    }

    @Override
    public List<Post> getAllWithUserRating(long limit, long offset, long userId) throws DaoException {
        String query = getSelectQuery() + ",post_rating.id,post_rating.post_id,post_rating.user_id,post_rating.delta,post_rating.date_ FROM post LEFT JOIN post_rating ON post.id=post_rating.post_id AND post_rating.user_id=?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
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
    public Post findById(Long id) throws DaoException {
        return super.findById(id);
    }

    @Override
    public Post bindData(ResultSet resultSet) throws DaoException {
        try {
            Post res = new Post();
            res.setId(resultSet.getLong("post.id"));
            res.setTitle(resultSet.getString("post.title"));
            res.setContent(resultSet.getString("post.content"));
            res.setPublicationDate(new Date(resultSet.getDate("post.date").getTime()));
            res.setType(resultSet.getInt("post.type"));
            res.setRating(resultSet.getLong("post.rating"));
            res.setAuthorId(resultSet.getLong("post.author_id"));

            return res;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Post bindDataWithRating(ResultSet resultSet) throws DaoException {
        try {
            Post post = bindData(resultSet);

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
    protected List<TableField> getTableFields() {

        return Arrays.asList(
                new TableField(TABLE_NAME, "id"),
                new TableField(TABLE_NAME, "title"),
                new TableField(TABLE_NAME, "type"),
                new TableField(TABLE_NAME, "content"),
                new TableField(TABLE_NAME, "date", "publicationDate").setTypeConverter(o -> new java.sql.Date(((Date) o).getTime())),
                new TableField(TABLE_NAME, "rating"),
                new TableField(TABLE_NAME, "author_id", "authorId")
        );
    }
}
