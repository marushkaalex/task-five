package com.epam.am.whatacat.action.admin;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.service.PostService;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.utils.ParameterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeletePostAction implements Action {
    public static final Logger LOG = LoggerFactory.getLogger(DeletePostAction.class);

    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        long id = ParameterUtils.parseLong(request.getParameter("id"), -1L);
        if (id == -1) {
            return new ActionResult(HttpServletResponse.SC_BAD_REQUEST);
        }

        try (PostService postService = new PostService()) {
            postService.delete(id);

            LOG.info("Post [{}] has been deleted", id);

            return new ActionResult("/admin/posts", true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
