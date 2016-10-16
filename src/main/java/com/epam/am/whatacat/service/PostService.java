package com.epam.am.whatacat.service;

import com.epam.am.whatacat.dao.CommentDao;
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

    /**
     * Finds post by its id
     * @param postId post id
     * @return Post or null if not found
     * @throws ServiceException
     */
    public Post findById(long postId) throws ServiceException {
        try {
            PostDao postDao = daoFactory.getPostDao();
            Post post = postDao.findById(postId);
            return post;
        } catch (DaoException e) {
            throw new ServiceException("Error while finding post", e);
        }
    }

    /**
     * Creates and saves a post
     * @param type type
     * @param title title
     * @param content text content
     * @param authorId id of the author
     * @return saved Post
     * @throws ServiceException
     */
    public Post createPost(int type, String title, String content, long authorId) throws ServiceException {
        try {
            PostDao postDao = daoFactory.getPostDao();
            Post post = new Post();
            post.setTitle(title);
            post.setAuthorId(authorId);
            post.setContent(content);
            post.setPublicationDate(new Date());
            post.setType(type);
            post.setStatus(Post.Status.ON_MODERATION);
            return postDao.save(post);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Counts posts with the status
     * @param status post status
     * @return number of posts with specified status
     * @throws ServiceException
     */
    public long countByStatus(Post.Status status) throws ServiceException {
        try {
            PostDao postDao = daoFactory.getPostDao();
            return postDao.countByStatus(status.getId());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Counts user posts of specified status or any status if parameter is null
     * @param userId id of the user
     * @param status posts status
     * @return number of posts of the user
     * @throws ServiceException
     */
    public long countUsersPosts(long userId, @Nullable Post.Status status) throws ServiceException {
        try {
            PostDao postDao = daoFactory.getPostDao();
            return postDao.countUsersPosts(userId,status == null ? null : status.getId());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     *
     * @param status post status
     * @param userId user id
     * @param limit limit
     * @param offset offset
     * @return post with specified status belong to the user or for all users if user id is not specified
     * @throws ServiceException
     */
    public List<Post> getPostListByStatus(Post.Status status, @Nullable Long userId, long limit, long offset) throws ServiceException {
        try {
            PostDao postDao = daoFactory.getPostDao();
            return postDao.getByStatus(status.getId(), userId, limit, offset);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Rates the post by the user
     * @param userId rater id
     * @param post post to be rated
     * @param ratingDelta how much rating will be added to the post
     * @throws ServiceException
     */
    public void rate(long userId, Post post, int ratingDelta) throws ServiceException {
        try {
            PostRating postRating = post.getUserPostRating();
            if (postRating == null) {
                postRating = new PostRating();
                postRating.setUserId(userId);
                postRating.setPostId(post.getId());
                postRating.setDate(new Date());
                postRating.setRatingDelta(ratingDelta);

                post.setRating(post.getRating() + ratingDelta);
            } else {
                if (postRating.getRatingDelta() == ratingDelta) return;
                post.setRating(post.getRating() + ratingDelta);
                postRating.setRatingDelta(postRating.getRatingDelta() + ratingDelta);
            }

            daoFactory.startTransaction();
            PostDao postDao = daoFactory.getPostDao();
            UserDao userDao = daoFactory.getUserDao();
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

    /**
     *
     * @param id post id
     * @return Post or null if not fount
     * @throws ServiceException
     */
    public Post getById(long id) throws ServiceException {
        try {
            return daoFactory.getPostDao().findById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Returns post with set UserPostRating
     * @param id post id
     * @param userId user id
     * @return Post or null if not found
     * @throws ServiceException
     */
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

    /**
     *
     * @return the number of posts
     * @throws ServiceException
     */
    public long count() throws ServiceException {
        try {
            PostDao postDao = daoFactory.getPostDao();
            return postDao.count();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Saves the post
     * @param post post to be saved
     * @throws ServiceException
     */
    public void save(Post post) throws ServiceException {
        try {
            PostDao postDao = daoFactory.getPostDao();
            postDao.save(post);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     *
     * @param userId user id
     * @param status post status
     * @param limit limit
     * @param offset offset
     * @return list of user posts with specified or any status (if the status is null)
     * @throws ServiceException
     */
    public List<Post> getAllOfUser(long userId, @Nullable Integer status, long limit, long offset) throws ServiceException {
        try {
            PostDao postDao = daoFactory.getPostDao();
            return postDao.getAllOfUser(userId, status, limit, offset);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     *
     * @param limit limit
     * @param offset offset
     * @return list of all posts
     * @throws ServiceException
     */
    public List<Post> getAll(long limit, long offset) throws ServiceException{
        try {
            PostDao postDao = daoFactory.getPostDao();
            return postDao.getAll(limit, offset);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Deletes post with specified id
     * @param id post ud
     * @throws ServiceException
     */
    public void delete(long id) throws ServiceException {
        try {
            daoFactory.startTransaction();
            CommentDao commentDao = daoFactory.getCommentDao();
            commentDao.deletePostComments(id);
            PostDao postDao = daoFactory.getPostDao();
            postDao.delete(id);
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
}
