package com.epam.am.whatacat.dao;

import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.model.PostRating;

import javax.annotation.Nullable;
import java.util.List;

public interface PostDao extends BaseDao<Post> {
    void rate(PostRating postRating) throws DaoException;

    PostRating getRating(long postId, long userId) throws DaoException;

    List<Post> getAllWithUserRating(long limit, long offset, long userId) throws DaoException;

    List<Post> getByStatus(int status, @Nullable Long userId, long limit, long offset) throws DaoException;

    Post findByIdWithRating(long postId, long userId) throws DaoException;

    long countByStatus(int status) throws DaoException;
}
