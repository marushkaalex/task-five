package com.epam.am.whatacat.dao.jdbc.binder;

import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.dao.jdbc.DataBinder;
import com.epam.am.whatacat.model.Gender;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.model.Role;
import com.epam.am.whatacat.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class PostDataBinder implements DataBinder<Post> {
    private DataBinder<User> userDataBinder = new UserDataBinder();

    @Override
    public Post bind(ResultSet resultSet) throws DaoException {
        try {
            Post res = new Post();
            res.setId(resultSet.getLong("post.id"));
            res.setTitle(resultSet.getString("post.title"));
            res.setContent(resultSet.getString("post.content"));
            res.setPublicationDate(new Date(resultSet.getDate("post.date").getTime()));
            res.setType(resultSet.getInt("post.type"));
            res.setRating(resultSet.getLong("post.rating"));
            res.setAuthorId(resultSet.getLong("post.author_id"));

            User user = userDataBinder.bind(resultSet);

            res.setAuthor(user);
            return res;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
