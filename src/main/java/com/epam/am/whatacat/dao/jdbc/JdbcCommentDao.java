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
                new TableField(TABLE_NAME, "id"),
                new TableField(TABLE_NAME, "author_id", "authorId"),
                new TableField(TABLE_NAME, "parent_id", "parentId"),
                new TableField(TABLE_NAME, "_date", "publicationDate").setTypeConverter(new DateTypeConverter()),
                new TableField(TABLE_NAME, "text"),
                new TableField(TABLE_NAME, "post_id", "postId"),
                new TableField(TABLE_NAME_USER, "id").setUseOnSave(false),
                new TableField(TABLE_NAME_USER, "email").setUseOnSave(false),
                new TableField(TABLE_NAME_USER, "nickname").setUseOnSave(false),
                new TableField(TABLE_NAME_USER, "password", "hashedPassword").setUseOnSave(false),
                new TableField(TABLE_NAME_USER, "role_id", "role").setTypeConverter(o -> ((Role) o).getId()).setUseOnSave(false),
                new TableField(TABLE_NAME_USER, "gender").setTypeConverter(o -> ((Gender) o).getKey()).setUseOnSave(false),
                new TableField(TABLE_NAME_USER, "rating").setUseOnSave(false),
                new TableField(TABLE_NAME_USER, "avatar", "avatarUrl").setUseOnSave(false),
                new TableField(TABLE_NAME_USER, "date", "registrationDate").setTypeConverter(new DateTypeConverter()).setUseOnSave(false),
                new TableField(TABLE_NAME_ROLE, "name").setUseOnSave(false)
        );
    }

    @Override
    protected String getJoin() {
        return " JOIN user ON " + TABLE_NAME + ".author_id=" + TABLE_NAME_USER + ".id JOIN role ON "
                + TABLE_NAME_ROLE + ".id=user.role_id";
    }

    @Override
    public List<Comment> getPostComments(long postId) throws DaoException {
        try (
                PreparedStatement preparedStatement =
                        getConnection().prepareStatement(getSelectQueryWithFrom() + getJoin() + " WHERE " + TABLE_NAME + ".post_id=?")
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
        try (PreparedStatement preparedStatement =
                     getConnection().prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE post_id=?")
        ) {
            preparedStatement.setLong(1, postId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void deleteUserComments(long postId) throws DaoException {
        try (PreparedStatement preparedStatement =
                     getConnection().prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE author_id=?")
        ) {
            preparedStatement.setLong(1, postId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
