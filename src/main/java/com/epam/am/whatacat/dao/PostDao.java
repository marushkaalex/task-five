package com.epam.am.whatacat.dao;

import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.model.PostRating;

public interface PostDao extends BaseDao<Post> {
    void rate(PostRating postRating) throws DaoException;

    PostRating getRating(long postId, long userId) throws DaoException;
}
