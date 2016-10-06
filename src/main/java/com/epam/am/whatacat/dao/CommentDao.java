package com.epam.am.whatacat.dao;

import com.epam.am.whatacat.model.Comment;

import java.util.List;

public interface CommentDao extends BaseDao<Comment> {
    public List<Comment> getPostComments(long postId) throws DaoException;
}
