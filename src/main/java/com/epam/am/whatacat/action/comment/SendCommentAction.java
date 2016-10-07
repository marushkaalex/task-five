package com.epam.am.whatacat.action.comment;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.model.Comment;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.CommentService;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.utils.ParameterUtils;
import com.epam.am.whatacat.validation.FormValidator;
import com.epam.am.whatacat.validation.FormValidatorFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public class SendCommentAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        FormValidator validator = FormValidatorFactory.getInstance().getValidator("send-comment");
        List<String> errorList = validator.validate(request.getParameterMap());
        if (!errorList.isEmpty()) {
            request.getSession().setAttribute("errorList", errorList);
            return new ActionResult("post?id=" + request.getParameter("post_id"), true);
        }

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return new ActionResult(HttpServletResponse.SC_UNAUTHORIZED);
        }
        long postId = ParameterUtils.parseLong(request.getParameter("post_id"), -1L);
        if (postId == -1) {
            return new ActionResult(HttpServletResponse.SC_BAD_REQUEST);
        }

        Comment comment = new Comment();
        comment.setAuthorId(user.getId());
        comment.setParentId(ParameterUtils.parseLong(request.getParameter("parent_id"), null));
        comment.setPostId(postId);
        comment.setText(request.getParameter("text"));
        comment.setPublicationDate(new Date());

        try (CommentService commentService = new CommentService()) {
            commentService.save(comment);
            return new ActionResult("post?id=" + request.getParameter("post_id"), true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
