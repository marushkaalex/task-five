package com.epam.am.whatacat.service;

import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.dao.DaoFactory;
import com.epam.am.whatacat.dao.PostDao;
import com.epam.am.whatacat.dao.UserDao;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.model.User;

public class PostService {
    public User getPostAuthor(long authorId) throws ServiceException {
        try (DaoFactory daoFactory = DaoFactory.getFactory()){
            UserDao userDao = daoFactory.getUserDao();
            User user = userDao.findById(authorId);
            return user;
        } catch (DaoException e) {
            throw new ServiceException("Error while getting post author", e);
        }
    }

    public Post findById(long postId) throws ServiceException {
        try (DaoFactory daoFactory = DaoFactory.getFactory()) {
            PostDao postDao = daoFactory.getPostDao();
            Post post = postDao.findById(postId);
            return post;
        } catch (DaoException e) {
            throw new ServiceException("Error while finding post", e);
        }
    }
}
