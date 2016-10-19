package com.epam.am.whatacat.action.post;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.BaseAction;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.PostService;
import com.epam.am.whatacat.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RatePostAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(RatePostAction.class);

    private static final String PARAMETER_POST_ID = "post_id";
    private static final String PARAMETER_DELTA = "delta";
    private static final String REDIRECT_URL = "post?id=";

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        long postId = Long.parseLong(request.getParameter(PARAMETER_POST_ID));
        User user = getUser(request);
        long userId = user.getId();
        int ratingDelta = Integer.parseInt(request.getParameter(PARAMETER_DELTA));
        try (PostService postService = new PostService()) {
            Post post = postService.getByIdWithRating(postId, userId);
            postService.rate(userId, post, ratingDelta);

            LOG.info("Post [{}] was rated by user [{}]: {}", postId, userId, ratingDelta);

            return new ActionResult(REDIRECT_URL + postId, true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
