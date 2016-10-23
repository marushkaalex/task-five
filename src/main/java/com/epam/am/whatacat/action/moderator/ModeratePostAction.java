package com.epam.am.whatacat.action.moderator;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.BaseAction;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.service.PostService;
import com.epam.am.whatacat.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ModeratePostAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(ModeratePostAction.class);

    private static final String REDIRECT_URL = "/post?id=";
    private static final String PARAMETER_DECISION = "decision";
    private static final String DECISION_ALLOW = "allow";
    private static final String DECISION_DENY = "deny";

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        String decision = request.getParameter(PARAMETER_DECISION);
        Post.Status status;
        switch (decision) {
            case DECISION_ALLOW:
                status = Post.Status.ALLOWED;
                break;
            case DECISION_DENY:
                status = Post.Status.DENIED;
                break;
            default:
                return getBadRequest();
        }
        long id = getIdParameter(request);
        if (id == INVALID_ID) {
            return getBadRequest();
        }

        try (PostService postService = new PostService()) {
            Post post = postService.findById(id);
            if (post == null) {
                return getBadRequest();
            }

            post.setStatus(status);
            postService.save(post);

            LOG.info("Post [{}] has been moderated. New status: {}", id, status);

            return new ActionResult(REDIRECT_URL + id, true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }

    private ActionResult getBadRequest() {
        return new ActionResult(HttpServletResponse.SC_BAD_REQUEST);
    }
}
