package com.epam.am.whatacat.dao.jdbc;

import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.dao.PostDao;
import com.epam.am.whatacat.dao.jdbc.binder.PostDataBinder;
import com.epam.am.whatacat.model.Gender;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.model.PostRating;
import com.epam.am.whatacat.model.Role;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class JdbcPostDao extends AbstractJdbcDao<Post> implements PostDao {

    private static final String TABLE_NAME = "post";
    private static final String TABLE_NAME_USER = "user";
    private static final String TABLE_NAME_ROLE = "role";
    private static final String TABLE_NAME_POST = "post";
    private static final String TABLE_NAME_POST_RATING = "post_rating";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_POST_ID = "post_id";
    private static final String COLUMN_DELTA = "delta";
    private static final String COLUMN_DATE_ = "date_";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_AUTHOR_ID = "author_id";
    private static final String COLUMN_ROLE_ID = "role_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_NICKNAME = "nickname";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_AVATAR = "avatar";
    private static final String COLUMN_NAME = "name";

    private DataBinder<Post> dataBinder = new PostDataBinder();

    public JdbcPostDao(Connection connection) {
        super(connection, Post.class);
    }

    @Override
    public String getTableName(boolean isInsert) {
        return TABLE_NAME;
    }

    @Override
    public DataBinder<Post> getDataBinder() {
        return dataBinder;
    }

    public Post bindDataWithRating(ResultSet resultSet) throws DaoException {
        try {
            Post post = dataBinder.bind(resultSet);

            PostRating postRating = new PostRating();
            postRating.setId(resultSet.getLong(makeTableField(TABLE_NAME_POST_RATING, COLUMN_ID)));
            postRating.setUserId(resultSet.getLong(makeTableField(TABLE_NAME_POST_RATING, COLUMN_USER_ID)));
            postRating.setPostId(resultSet.getLong(makeTableField(TABLE_NAME_POST_RATING, COLUMN_POST_ID)));
            postRating.setRatingDelta(resultSet.getInt(makeTableField(TABLE_NAME_POST_RATING, COLUMN_DELTA)));
            java.sql.Timestamp date = resultSet.getTimestamp(makeTableField(TABLE_NAME_POST_RATING, COLUMN_DATE_));
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
        try (
                PreparedStatement preparedStatement = postRating.getId() == null
                        ? getConnection().prepareStatement(String.format("INSERT INTO %s(%s,%s,%s,%s) VALUES(?, ?, ?, ?)", TABLE_NAME_POST_RATING, COLUMN_POST_ID, COLUMN_USER_ID, COLUMN_DELTA, COLUMN_DATE_))
                        : getConnection().prepareStatement(String.format("UPDATE %s SET %s=?,%s=?,%s=?,%s=? WHERE %s=?", TABLE_NAME_POST_RATING, COLUMN_POST_ID, COLUMN_USER_ID, COLUMN_DELTA, COLUMN_DATE_, COLUMN_ID))
        ) {

            if (postRating.getId() == null) {
                preparedStatement.setLong(1, postRating.getPostId());
                preparedStatement.setLong(2, postRating.getUserId());
                preparedStatement.setInt(3, postRating.getRatingDelta());
                preparedStatement.setTimestamp(4, new java.sql.Timestamp(new Date().getTime()));
                preparedStatement.execute();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    postRating.setId(generatedKeys.getLong(1));
                }
                generatedKeys.close();
            } else {
                preparedStatement.setLong(1, postRating.getPostId());
                preparedStatement.setLong(2, postRating.getUserId());
                preparedStatement.setInt(3, postRating.getRatingDelta());
                preparedStatement.setTimestamp(4, new java.sql.Timestamp(new Date().getTime()));
                preparedStatement.setLong(5, postRating.getId());

                preparedStatement.execute();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public PostRating getRating(long postId, long userId) throws DaoException {
        try (
                PreparedStatement preparedStatement = getConnection().prepareStatement(String.format("SELECT %s, %s, %s FROM %s WHERE %s=? AND %s=?", COLUMN_ID, COLUMN_DELTA, COLUMN_DATE_, TABLE_NAME_POST_RATING, COLUMN_POST_ID, COLUMN_USER_ID))
        ) {

            preparedStatement.setLong(1, postId);
            preparedStatement.setLong(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            PostRating rating = null;
            if (resultSet.next()) {
                rating = new PostRating();
                rating.setId(resultSet.getLong(COLUMN_ID));
                rating.setPostId(postId);
                rating.setUserId(userId);
                rating.setRatingDelta(resultSet.getInt(COLUMN_DELTA));
                java.sql.Timestamp date_ = resultSet.getTimestamp(COLUMN_DATE_);
                rating.setDate(new Date(date_.getTime()));
            }

            resultSet.close();

            return rating;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected String getOrderBy() {
        return " ORDER BY " + makeTableField(TABLE_NAME_POST, COLUMN_DATE) + " DESC";
    }

    @Override
    public long countByStatus(int status) throws DaoException {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(String.format("SELECT COUNT(1) FROM %s WHERE %s=?", getTableName(false), makeTableField(TABLE_NAME, COLUMN_STATUS)));
            preparedStatement.setInt(1, status);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            long count = resultSet.getLong(1);
            resultSet.close();
            return count;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected String getJoin() {
//        return " JOIN " + TABLE_NAME_USER + " ON " + makeTableField(TABLE_NAME_POST, "author_id") + "=" + makeTableField(TA
// BLE_NAME_USER, COLUMN_ID) + " JOIN " + TABLE_NAME_ROLE + " ON " + makeTableField(TABLE_NAME_USER, "role_id") + "=" + makeTableField(TABLE_NAME_ROLE, COLUMN_ID);
        return String.format(" JOIN %s ON %s=%s JOIN %s ON %s=%s", TABLE_NAME_USER, makeTableField(TABLE_NAME_POST, COLUMN_AUTHOR_ID), makeTableField(TABLE_NAME_USER, COLUMN_ID), TABLE_NAME_ROLE, makeTableField(TABLE_NAME_USER, COLUMN_ROLE_ID), makeTableField(TABLE_NAME_ROLE, COLUMN_ID));
    }

    @Override
    protected List<TableField> getTableFields() {

        return Arrays.asList(
                new TableField(TABLE_NAME, COLUMN_ID),
                new TableField(TABLE_NAME, COLUMN_TITLE),
                new TableField(TABLE_NAME, COLUMN_TYPE),
                new TableField(TABLE_NAME, COLUMN_CONTENT),
                new TableField(TABLE_NAME, COLUMN_DATE, "publicationDate").setTypeConverter(o -> new java.sql.Timestamp(((Date) o).getTime())),
                new TableField(TABLE_NAME, COLUMN_RATING),
                new TableField(TABLE_NAME, COLUMN_AUTHOR_ID, "authorId"),
                new TableField(TABLE_NAME, COLUMN_STATUS).setTypeConverter(o -> ((Post.Status) o).getId()),
                new TableField(TABLE_NAME_USER, COLUMN_ID).setUseOnSave(false),
                new TableField(TABLE_NAME_USER, COLUMN_EMAIL).setUseOnSave(false),
                new TableField(TABLE_NAME_USER, COLUMN_NICKNAME).setUseOnSave(false),
                new TableField(TABLE_NAME_USER, COLUMN_PASSWORD, "hashedPassword").setUseOnSave(false),
                new TableField(TABLE_NAME_USER, COLUMN_ROLE_ID, "role").setTypeConverter(o -> ((Role) o).getId()).setUseOnSave(false),
                new TableField(TABLE_NAME_USER, COLUMN_GENDER).setTypeConverter(o -> ((Gender) o).getKey()).setUseOnSave(false),
                new TableField(TABLE_NAME_USER, COLUMN_RATING).setUseOnSave(false),
                new TableField(TABLE_NAME_USER, COLUMN_AVATAR, "avatarUrl").setUseOnSave(false),
                new TableField(TABLE_NAME_USER, COLUMN_DATE, "registrationDate").setTypeConverter(o -> new java.sql.Timestamp(((Date) o).getTime())).setUseOnSave(false),
                new TableField(TABLE_NAME_ROLE, COLUMN_NAME).setUseOnSave(false).setUseOnSave(false)
        );
    }

    public List<Post> getByStatusWithUserPostRating(int status, long userId, long limit, long offset) throws DaoException {
        StringBuilder query = new StringBuilder()
                .append(getSelectQuery())
//                .append(",post_rating.id,post_rating.post_id,post_rating.user_id,post_rating.delta,post_rating.date_ FROM post LEFT JOIN post_rating ON post.id=post_rating.post_id AND post_rating.user_id=?")
                .append(',')
                .append(makeTableField(TABLE_NAME_POST_RATING, COLUMN_ID))
                .append(',')
                .append(makeTableField(TABLE_NAME_POST_RATING, COLUMN_POST_ID))
                .append(',')
                .append(makeTableField(TABLE_NAME_POST_RATING, COLUMN_USER_ID))
                .append(',')
                .append(makeTableField(TABLE_NAME_POST_RATING, COLUMN_DELTA))
                .append(',')
                .append(makeTableField(TABLE_NAME_POST_RATING, COLUMN_DATE_))
                .append(" FROM ")
                .append(TABLE_NAME_POST)
                .append(" LEFT JOIN ")
                .append(TABLE_NAME_POST_RATING)
                .append(" ON ")
                .append(makeTableField(TABLE_NAME_POST, COLUMN_ID))
                .append('=')
                .append(makeTableField(TABLE_NAME_POST_RATING, COLUMN_POST_ID))
                .append(" AND ")
                .append(makeTableField(TABLE_NAME_POST_RATING, COLUMN_USER_ID))
                .append("=?")
                .append(getJoin())
                .append(" WHERE post.status=?")
                .append(getOrderBy())
                .append("  LIMIT ? OFFSET ?");

        // iw will be something like:
        // SELECT post.id,post.title,post.type,post.content,post.date,post.rating,post.author_id,post.status,user.id,user.email,user.nickname,user.password,user.role_id,user.gender,user.rating,user.avatar,user.date,role.name,post_rating.id,post_rating.post_id,post_rating.user_id,post_rating.delta,post_rating.date_ FROM post LEFT JOIN post_rating ON post.id=post_rating.post_id AND post_rating.user_id=? JOIN user ON post.author_id=user.id JOIN role ON user.role_id=role.id WHERE post.status=? ORDER BY post.date DESC  LIMIT ? OFFSET ?
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query.toString())) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, status);
            preparedStatement.setLong(3, limit);
            preparedStatement.setLong(4, offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Post> res = new ArrayList<>();
            while (resultSet.next()) {
                Post post = bindDataWithRating(resultSet);
                res.add(post);
            }

            resultSet.close();
            return res;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<Post> getByStatusWithoutUserPostRating(int status, long limit, long offset) throws DaoException {
        StringBuilder query = new StringBuilder()
                .append(getSelectQueryWithFrom())
                .append(getJoin())
                .append(" WHERE ")
                .append(makeTableField(TABLE_NAME, COLUMN_STATUS))
                .append("=?")
                .append(getOrderBy())
                .append(" LIMIT ? OFFSET ?");

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query.toString())) {
            preparedStatement.setLong(1, status);
            preparedStatement.setLong(2, limit);
            preparedStatement.setLong(3, offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Post> res = new ArrayList<>();
            while (resultSet.next()) {
                Post post = getDataBinder().bind(resultSet);
                res.add(post);
            }

            resultSet.close();
            return res;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Post> getByStatus(int status, @Nullable Long userId, long limit, long offset) throws DaoException {
        if (userId == null) {
            return getByStatusWithoutUserPostRating(status, limit, offset);
        } else {
            return getByStatusWithUserPostRating(status, userId, limit, offset);
        }
    }

    @Override
    public List<Post> getAllOfUser(long userId, @Nullable Integer status, long limit, long offset) throws DaoException {
        StringBuilder query = new StringBuilder()
                .append(getSelectQueryWithFrom())
                .append(getJoin())
                .append(" WHERE ")
                .append(makeTableField(TABLE_NAME, COLUMN_AUTHOR_ID))
                .append("=?");

        if (status != null) {
            query
                    .append("AND ")
                    .append(makeTableField(TABLE_NAME, COLUMN_STATUS))
                    .append("=?");
        }

        query
                .append(getOrderBy())
                .append(" LIMIT ? OFFSET ?");

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query.toString())) {
            preparedStatement.setLong(1, userId);
            if (status != null) {
                preparedStatement.setLong(2, status);
                preparedStatement.setLong(3, limit);
                preparedStatement.setLong(4, offset);
            } else {
                preparedStatement.setLong(2, limit);
                preparedStatement.setLong(3, offset);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Post> res = new ArrayList<>();
            while (resultSet.next()) {
                Post post = getDataBinder().bind(resultSet);
                res.add(post);
            }
            resultSet.close();
            return res;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public long countUsersPosts(long userId, @Nullable Integer status) throws DaoException {
        try {
            StringBuilder query = new StringBuilder()
                    .append("SELECT COUNT(1) FROM ")
                    .append(TABLE_NAME)
                    .append(" WHERE ")
                    .append(makeTableField(TABLE_NAME, COLUMN_AUTHOR_ID))
                    .append("=?");

            if (status != null) {
                query
                        .append(" AND ")
                        .append(makeTableField(TABLE_NAME, COLUMN_STATUS))
                        .append("=?");
            }

            PreparedStatement preparedStatement = getConnection().prepareStatement(query.toString());
            preparedStatement.setLong(1, userId);

            if (status != null) {
                preparedStatement.setInt(2, status);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            long count = resultSet.getLong(1);
            resultSet.close();
            return count;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(long id) throws DaoException {
        Connection connection = getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + TABLE_NAME_POST_RATING + " WHERE " + COLUMN_POST_ID + "=?");
            preparedStatement.setLong(1, id);
            preparedStatement.execute();

            super.delete(id);

            getConnection().commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException e1) {
                throw new DaoException(e1);
            }
            throw new DaoException(e);
        }
    }

    @Override
    public void deleteUserPosts(long userId) throws DaoException {
        try {
            PreparedStatement preparedStatement =
                    getConnection().prepareStatement(String.format("SELECT %s FROM %s WHERE %s=?", COLUMN_ID, TABLE_NAME, COLUMN_AUTHOR_ID));
            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                delete(resultSet.getLong(COLUMN_ID));
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
