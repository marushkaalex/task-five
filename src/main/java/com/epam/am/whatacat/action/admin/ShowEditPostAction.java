package com.epam.am.whatacat.action.admin;

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

public class ShowEditPostAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(ShowEditPostAction.class);

    private static final String VIEW = "edit-post";

    private static final String ATTRIBUTE_POST = "post";
    private static final String ATTRIBUTE_STATUSES = "statuses";

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        long id = getIdParameter(request);
        if (id == INVALID_ID) {
            return new ActionResult(HttpServletResponse.SC_NOT_FOUND);
        }

        try (PostService postService = new PostService()) {

            Post post = postService.findById(id);
            request.setAttribute(ATTRIBUTE_POST, post);
            request.setAttribute(ATTRIBUTE_STATUSES, Post.Status.values());

            LOG.info("Edit post form has been shown for post [{}]", id);

            return new ActionResult(VIEW);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
