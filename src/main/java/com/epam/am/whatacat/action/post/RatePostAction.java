package com.epam.am.whatacat.action.post;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.model.PostRating;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.PostService;
import com.epam.am.whatacat.service.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RatePostAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        String idParameter = request.getParameter("id");
        Long id = null;
        if (idParameter != null && !idParameter.isEmpty()) {
            id = Long.parseLong(idParameter);
        }
        long postId = Long.parseLong(request.getParameter("post_id"));
        User user = (User) request.getSession().getAttribute("user");
        long userId = user.getId();
        int ratingDelta = Integer.parseInt(request.getParameter("delta"));
        // TODO: 11.09.2016 check user permissions
        PostRating postRating = new PostRating();
        postRating.setId(id);
        postRating.setPostId(postId);
        postRating.setUserId(userId);
        postRating.setRatingDelta(ratingDelta);
        try (PostService postService = new PostService()) {
            Post post = postService.getById(postId);
            postService.rate(post, postRating);
            return new ActionResult("post?id=" + postId, true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
