package com.epam.am.whatacat.dao;

import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.model.PostRating;

import javax.annotation.Nullable;
import java.util.List;

public interface PostDao extends BaseDao<Post> {
    void rate(PostRating postRating) throws DaoException;

    PostRating getRating(long postId, long userId) throws DaoException;

    List<Post> getByStatus(int status, @Nullable Long userId, long limit, long offset) throws DaoException;

    List<Post> getAllOfUser(long userId, @Nullable Integer status, long limit, long offset) throws DaoException;

    long countByStatus(int status) throws DaoException;

    long countUsersPosts(long user, @Nullable Integer status) throws DaoException;

    void deleteUserPosts(long user) throws DaoException;
}
