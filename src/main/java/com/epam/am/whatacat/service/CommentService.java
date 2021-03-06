package com.epam.am.whatacat.service;

import com.epam.am.whatacat.dao.CommentDao;
import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.model.Comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentService extends BaseService {
    public CommentService() throws ServiceException {
    }

    /**
     *
     * @param postId id of the post
     * @return List of post comments
     * @throws ServiceException
     */
    public List<Comment> getPostComments(long postId) throws ServiceException {
        try {
            CommentDao commentDao = daoFactory.getCommentDao();
            List<Comment> postComments = commentDao.getPostComments(postId);
            setCommentsParents(postComments);
            return postComments;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private void setCommentsParents(List<Comment> commentList) {
        Map<Long, Comment> idCommentMap = new HashMap<>();
        for (Comment comment : commentList) {
            idCommentMap.put(comment.getId(), comment);
        }

        for (Comment comment : commentList) {
            if (comment.getParentId() != null) {
                comment.setParent(idCommentMap.get(comment.getParentId()));
            }
        }
    }

    /**
     * Saves comment
     * @param comment comment to save
     * @throws ServiceException
     */
    public void save(Comment comment) throws ServiceException {
        try {
            CommentDao commentDao = daoFactory.getCommentDao();
            commentDao.save(comment);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Deletes comment
     * @param id comment to be deleted
     * @throws ServiceException
     */
    public void delete(long id) throws ServiceException {
        try {
            CommentDao commentDao = daoFactory.getCommentDao();
            commentDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
