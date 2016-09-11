package com.epam.am.whatacat.service;

import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.dao.PostDao;
import com.epam.am.whatacat.dao.UserDao;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.model.PostRating;
import com.epam.am.whatacat.model.User;

import java.util.Date;
import java.util.List;

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

    public List<Post> getPostList(long limit, long offset) throws ServiceException {
        try {
            PostDao postDao = daoFactory.getPostDao();
            return postDao.getAll(limit, offset);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Post> getPostListWithRating(long userId, long limit, long offset) throws ServiceException {
        try {
            PostDao postDao = daoFactory.getPostDao();
            List<Post> postList = postDao.getAll(limit, offset);
            for (Post post : postList) {
                // TODO: 11.09.2016 use only one SQL query
                post.setUserPostRating(postDao.getRating(post.getId(), userId));
            }
            return postList;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    // TODO: 11.09.2016 check user permissions
    public void rate(PostRating postRating) throws ServiceException {
        try {
            PostDao postDao = daoFactory.getPostDao();
            postDao.rate(postRating);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
