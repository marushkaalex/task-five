package com.epam.am.whatacat.dao.jdbc.binder;

import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.dao.jdbc.DataBinder;
import com.epam.am.whatacat.model.Comment;
import com.epam.am.whatacat.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class CommentDataBinder implements DataBinder<Comment> {
    private DataBinder<User> userDataBinder = new UserDataBinder();

    @Override
    public Comment bind(ResultSet resultSet) throws DaoException {
        try {
            Comment res = new Comment();
            res.setId(resultSet.getLong("comment.id"));
            res.setAuthorId(resultSet.getLong("comment.author_id"));
            res.setParentId(resultSet.getLong("comment.parent_id"));
            res.setPublicationDate(new Date(resultSet.getTimestamp("_date").getTime()));
            res.setText(resultSet.getString("comment.text"));

            User author = userDataBinder.bind(resultSet);
            res.setAuthor(author);
            return res;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
