package com.epam.am.whatacat.action.comment;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.service.CommentService;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.utils.ParameterUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteCommentAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        long id = ParameterUtils.parseLong(request.getParameter("id"), -1L);
        if (id == -1) {
            return new ActionResult(HttpServletResponse.SC_BAD_REQUEST);
        }

        try (CommentService commentService = new CommentService()) {
            commentService.delete(id);
            String referer = request.getHeader("Referer");
            return new ActionResult(referer, true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
