package com.epam.am.whatacat.action.get;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.service.PostService;
import com.epam.am.whatacat.service.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowPostAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        String idParameter = request.getParameter("id");
        Long id = Long.parseLong(idParameter);

        try (PostService postService = new PostService()) {
            Post post = postService.getById(id);
            request.setAttribute("post", post);
            return new ActionResult("post");
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
