package com.epam.am.whatacat.dao.jdbc;

import com.epam.am.whatacat.dao.CommentDao;
import com.epam.am.whatacat.dao.jdbc.binder.CommentDataBinder;
import com.epam.am.whatacat.model.Comment;
import com.epam.am.whatacat.model.Gender;
import com.epam.am.whatacat.model.Role;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

public class JdbcCommentDao extends AbstractJdbcDao<Comment> implements CommentDao {
    public static final String TABLE_NAME = "comment";

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
                new TableField(TABLE_NAME, "_date", "publicationDate").setTypeConverter(new DateTypeConterter()),
                new TableField(TABLE_NAME, "text"),
                new TableField("user", "id").setUseOnSave(false),
                new TableField("user", "email").setUseOnSave(false),
                new TableField("user", "nickname").setUseOnSave(false),
                new TableField("user", "password", "hashedPassword").setUseOnSave(false),
                new TableField("user", "role_id", "role").setTypeConverter(o -> ((Role) o).getId()).setUseOnSave(false),
                new TableField("user", "gender").setTypeConverter(o -> ((Gender) o).getKey()).setUseOnSave(false),
                new TableField("user", "rating").setUseOnSave(false),
                new TableField("user", "avatar", "avatarUrl").setUseOnSave(false),
                new TableField("user", "date", "registrationDate").setTypeConverter(new DateTypeConterter()).setUseOnSave(false),
                new TableField("role", "name").setUseOnSave(false)
        );
    }
}
