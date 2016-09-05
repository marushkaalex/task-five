package com.epam.am.whatacat.service;

import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.dao.PostDao;
import com.epam.am.whatacat.dao.UserDao;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.model.User;

import java.util.Date;

public class PostService extends BaseService {
    public PostService() throws ServiceException {
    }

    public User getPostAuthor(long authorId) throws ServiceException {
        try {
            UserDao userDao = daoFactory.getUserDao();
            User user = userDao.findById(authorId);
            return user;
        } catch (DaoException e) {
            throw new ServiceException("Error while getting post author", e);
        }
    }

    public Post findById(long postId) throws ServiceException {
        try {
            PostDao postDao = daoFactory.getPostDao();
            Post post = postDao.findById(postId);
            return post;
        } catch (DaoException e) {
            throw new ServiceException("Error while finding post", e);
        }
    }

    public Post createPost(int type, String title, String content, long authorId) throws ServiceException {
        try {
            PostDao postDao = daoFactory.getPostDao();
            Post post = new Post();
            post.setTitle(title);
            post.setAuthorId(authorId);
            post.setContent(content);
            post.setPublicationDate(new Date());
            post.setType(type);
            return postDao.save(post);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
