package com.epam.am.whatacat.action.admin;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.ErrorHandlingAction;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.service.PostService;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.utils.ParameterUtils;
import com.epam.am.whatacat.validation.FormValidator;
import com.epam.am.whatacat.validation.FormValidatorFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class EditPostAction extends ErrorHandlingAction {
    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        long id = ParameterUtils.parseLong(request.getParameter("id"), -1L);
        if (id == -1) {
            return new ActionResult(HttpServletResponse.SC_BAD_REQUEST);
        }

        Post.Status status = Post.Status.valueOf(request.getParameter("status"));

        FormValidator validator = FormValidatorFactory.getInstance().getValidator("post");
        Map<String, String> errorMap = validator.validate(request.getParameterMap());

        long rating = ParameterUtils.parseLong(request.getParameter("rating"), Long.MIN_VALUE);
        if (rating == Long.MIN_VALUE) {
            errorMap.put("rating", "admin.posts.error.rating");
        }

        if (!errorMap.isEmpty()) {
            request.getSession().setAttribute("errorMap", errorMap);
            return new ActionResult("/admin/edit-post?id=" + id, true);
        }

        try (PostService postService = new PostService()) {
            Post post = postService.findById(id);
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            post.setTitle(title);
            post.setContent(content);
            post.setStatus(status);
            post.setRating(rating);
            postService.save(post);

            errorMap.put("success", "admin.posts.changes-saved-successfully");
            request.getSession().setAttribute("errorMap", errorMap);

            return new ActionResult("/admin/edit-post?id=" + id, true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
