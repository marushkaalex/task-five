package com.epam.am.whatacat.action.admin;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.ErrorHandlingAction;
import com.epam.am.whatacat.service.PostService;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.utils.ParameterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeletePostAction extends ErrorHandlingAction {
    private static final Logger LOG = LoggerFactory.getLogger(DeletePostAction.class);
    private static final String REDIRECT_URL = "/admin/posts";

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        long id = getIdParameter(request);
        if (id == INVALID_ID) {
            return new ActionResult(HttpServletResponse.SC_BAD_REQUEST);
        }

        try (PostService postService = new PostService()) {
            postService.delete(id);

            LOG.info("Post [{}] has been deleted", id);

            return new ActionResult(REDIRECT_URL, true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
