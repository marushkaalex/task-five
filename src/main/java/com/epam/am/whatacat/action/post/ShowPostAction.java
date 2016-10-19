package com.epam.am.whatacat.action.post;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.BaseAction;
import com.epam.am.whatacat.model.Comment;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.model.Role;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.CommentService;
import com.epam.am.whatacat.service.PostService;
import com.epam.am.whatacat.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ShowPostAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(ShowPostAction.class);

    private static final String PARAMETER_POST = "post";
    private static final String PARAMETER_COMMENTS = "comments";
    private static final String VIEW = "post";

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        long id = getIdParameter(request);

        try (
                PostService postService = new PostService();
                CommentService commentService = new CommentService()
        ) {
            User user = getUser(request);
            Post post = user == null ? postService.getById(id) : postService.getByIdWithRating(id, user.getId());

            if (!checkRights(user, post)) {
                return new ActionResult(HttpServletResponse.SC_NOT_FOUND);
            }

            request.setAttribute(PARAMETER_POST, post);

            List<Comment> postComments = commentService.getPostComments(id);
            request.setAttribute(PARAMETER_COMMENTS, postComments);

            LOG.info("Post [{}] has been shown", id);

            return new ActionResult(VIEW);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }

    private boolean checkRights(User user, Post post) {
        if (post.getStatus() == Post.Status.ALLOWED) {
            return true;
        }

        if (user == null) {
            return false;
        }

        if (user.getId() == post.getAuthorId()) {
            return true;
        }

        return user.getRole() == Role.MODERATOR || user.getRole() == Role.ADMIN;
    }
}
