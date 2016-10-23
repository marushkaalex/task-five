package com.epam.am.whatacat.dao.jdbc;

import com.epam.am.whatacat.dao.CommentDao;
import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.dao.jdbc.binder.CommentDataBinder;
import com.epam.am.whatacat.model.Comment;
import com.epam.am.whatacat.model.Gender;
import com.epam.am.whatacat.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JdbcCommentDao extends AbstractJdbcDao<Comment> implements CommentDao {
    private static final String TABLE_NAME = "comment";
    private static final String TABLE_NAME_USER = "user";
    private static final String TABLE_NAME_ROLE = "role";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_AUTHOR_ID = "author_id";
    private static final String COLUMN_PARENT_ID = "parent_id";
    private static final String COLUMN__DATE = "_date";
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_POST_ID = "post_id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_NICKNAME = "nickname";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE_ID = "role_id";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_AVATAR = "avatar";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_NAME = "name";

    private DataBinder<Comment> dataBinder = new CommentDataBinder();

    public JdbcCommentDao(Connection connection) {
        super(connection, Comment.class);
    }

    @Override
    public String getTableName(boolean isInsert) {
        return TABLE_NAME;
    }

    @Override
    public DataBinder<Comment> getDataBinder() {
        return dataBinder;
    }

    @Override
    protected List<TableField> getTableFields() {
        return Arrays.asList(
                new TableField(TABLE_NAME, COLUMN_ID),
                new TableField(TABLE_NAME, COLUMN_AUTHOR_ID, "authorId"),
                new TableField(TABLE_NAME, COLUMN_PARENT_ID, "parentId"),
                new TableField(TABLE_NAME, COLUMN__DATE, "publicationDate").setTypeConverter(new DateTypeConverter()),
                new TableField(TABLE_NAME, COLUMN_TEXT),
                new TableField(TABLE_NAME, COLUMN_POST_ID, "postId"),
                new TableField(TABLE_NAME_USER, COLUMN_ID).setUseOnSave(false),
                new TableField(TABLE_NAME_USER, COLUMN_EMAIL).setUseOnSave(false),
                new TableField(TABLE_NAME_USER, COLUMN_NICKNAME).setUseOnSave(false),
                new TableField(TABLE_NAME_USER, COLUMN_PASSWORD, "hashedPassword").setUseOnSave(false),
                new TableField(TABLE_NAME_USER, COLUMN_ROLE_ID, "role").setTypeConverter(o -> ((Role) o).getId()).setUseOnSave(false),
                new TableField(TABLE_NAME_USER, COLUMN_GENDER).setTypeConverter(o -> ((Gender) o).getKey()).setUseOnSave(false),
                new TableField(TABLE_NAME_USER, COLUMN_RATING).setUseOnSave(false),
                new TableField(TABLE_NAME_USER, COLUMN_AVATAR, "avatarUrl").setUseOnSave(false),
                new TableField(TABLE_NAME_USER, COLUMN_DATE, "registrationDate").setTypeConverter(new DateTypeConverter()).setUseOnSave(false),
                new TableField(TABLE_NAME_ROLE, COLUMN_NAME).setUseOnSave(false)
        );
    }

    @Override
    protected String getJoin() {
        return String.format(
                " JOIN %s ON %s=%s JOIN %s ON %s=%s",
                TABLE_NAME_USER,
                makeTableField(TABLE_NAME, COLUMN_AUTHOR_ID),
                makeTableField(TABLE_NAME_USER, COLUMN_ID),
                TABLE_NAME_ROLE,
                makeTableField(TABLE_NAME_ROLE, COLUMN_ID),
                makeTableField(TABLE_NAME_USER, COLUMN_ROLE_ID)
        );
    }

    @Override
    public List<Comment> getPostComments(long postId) throws DaoException {
        try (
                PreparedStatement preparedStatement =
                        getConnection().prepareStatement(getSelectQueryWithFrom() + getJoin() + " WHERE " + makeTableField(TABLE_NAME, COLUMN_POST_ID) + "=?")
        ) {
            preparedStatement.setLong(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Comment> res = new ArrayList<>();
            while (resultSet.next()) {
                res.add(dataBinder.bind(resultSet));
            }
            resultSet.close();
            return res;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void deletePostComments(long postId) throws DaoException {
        deleteCommentsByIdOfColumn(COLUMN_POST_ID, postId);
    }

    @Override
    public void deleteUserComments(long authorId) throws DaoException {
        deleteCommentsByIdOfColumn(COLUMN_AUTHOR_ID, authorId);
    }

    private void deleteCommentsByIdOfColumn(String columnName, long id) throws DaoException {
        try (PreparedStatement preparedStatement =
                     getConnection().prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE " + columnName + "=?")
        ) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
