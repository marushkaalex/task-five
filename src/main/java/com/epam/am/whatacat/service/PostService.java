package com.epam.am.whatacat.service;

import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.dao.PostDao;
import com.epam.am.whatacat.dao.UserDao;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.model.PostRating;
import com.epam.am.whatacat.model.User;

import javax.annotation.Nullable;
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

    public List<Post> getPostList(long limit, long offset, @Nullable Long userId) throws ServiceException {
        try {
            PostDao postDao = daoFactory.getPostDao();
            return userId == null ? postDao.getAll(limit, offset) : postDao.getAllWithUserRating(limit, offset, userId);
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
    public void rate(long userId, Post post, int ratingDelta) throws ServiceException {
        try {
            PostRating postRating = post.getUserPostRating();
            if (postRating == null) {
                postRating = new PostRating();;
                postRating.setUserId(userId);
                postRating.setPostId(post.getId());
                postRating.setDate(new Date());
                postRating.setRatingDelta(ratingDelta);

                post.setRating(post.getRating() + ratingDelta);
            } else {
//                post.setRating(post.getRating() - postRating.getRatingDelta());
                if (postRating.getRatingDelta() == ratingDelta) return;
                post.setRating(post.getRating() + ratingDelta);
                postRating.setRatingDelta(postRating.getRatingDelta() + ratingDelta);
            }

            daoFactory.startTransaction();
            PostDao postDao = daoFactory.getPostDao();
            UserDao userDao = daoFactory.getUserDao();
            // TODO: 13.09.2016 allow rate only once
            postDao.save(post);
            postDao.rate(postRating);

            User user = userDao.findById(post.getAuthorId());
            user.setRating(user.getRating() + ratingDelta);
            userDao.save(user);
            daoFactory.commitTransaction();
        } catch (DaoException e) {
            try {
                daoFactory.rollbackTransaction();
            } catch (DaoException e1) {
                throw new ServiceException(e1);
            }
            throw new ServiceException(e);
        }
    }

    public Post getById(long id) throws ServiceException {
        try {
            return daoFactory.getPostDao().findById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Post getByIdWithRating(long id, long userId) throws ServiceException {
        try {
            PostDao postDao = daoFactory.getPostDao();
            Post post = postDao.findById(id);
            PostRating rating = postDao.getRating(id, userId);
            post.setUserPostRating(rating);
            return post;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
