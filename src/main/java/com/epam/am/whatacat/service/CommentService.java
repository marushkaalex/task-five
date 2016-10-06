package com.epam.am.whatacat.service;

import com.epam.am.whatacat.dao.CommentDao;
import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.model.Comment;

import java.util.List;

public class CommentService extends BaseService {
    public CommentService() throws ServiceException {
    }

    public List<Comment> getPostComments(long postId) throws ServiceException {
        try {
            CommentDao commentDao = daoFactory.getCommentDao();
            return commentDao.getPostComments(postId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
