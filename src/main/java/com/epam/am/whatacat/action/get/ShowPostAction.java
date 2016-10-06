package com.epam.am.whatacat.action.get;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.model.Comment;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.CommentService;
import com.epam.am.whatacat.service.PostService;
import com.epam.am.whatacat.service.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ShowPostAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        String idParameter = request.getParameter("id");
        Long id = Long.parseLong(idParameter);

        try (
                PostService postService = new PostService();
                CommentService commentService = new CommentService()
        ) {
            User user = ((User) request.getSession().getAttribute("user"));
            Post post = user == null ? postService.getById(id) : postService.getByIdWithRating(id, user.getId());
            request.setAttribute("post", post);

            List<Comment> postComments = commentService.getPostComments(id);
            request.setAttribute("comments", postComments);
            return new ActionResult("post");
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
