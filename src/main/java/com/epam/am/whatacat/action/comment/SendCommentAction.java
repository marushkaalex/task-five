package com.epam.am.whatacat.action.comment;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.BaseAction;
import com.epam.am.whatacat.model.Comment;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.CommentService;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.validation.FormValidator;
import com.epam.am.whatacat.validation.FormValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

public class SendCommentAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(SendCommentAction.class);

    private static final String VALIDATOR = "send-comment";
    private static final String REDIRECT_URL = "post?id=";

    private static final String PARAMETER_POST_ID = "post_id";
    private static final String PARAMETER_PARENT_ID = "parent_id";
    private static final String PARAMETER_TEXT = "text";

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        long postId = getLongParameter(request, PARAMETER_POST_ID, INVALID_ID);
        if (postId == INVALID_ID) {
            return new ActionResult(HttpServletResponse.SC_BAD_REQUEST);
        }

        FormValidator validator = FormValidatorFactory.getInstance().getValidator(VALIDATOR);
        Map<String, String> errorMap = validator.validate(request.getParameterMap());
        if (!errorMap.isEmpty()) {
            setErrorMap(request, errorMap);
            return new ActionResult(REDIRECT_URL + postId, true);
        }

        User user = getUser(request);
        if (user == null) {
            return new ActionResult(HttpServletResponse.SC_UNAUTHORIZED);
        }

        Comment comment = new Comment();
        comment.setAuthorId(user.getId());
        comment.setParentId(getLongParameter(request, PARAMETER_PARENT_ID, null));
        comment.setPostId(postId);
        comment.setText(request.getParameter(PARAMETER_TEXT));
        comment.setPublicationDate(new Date());

        try (CommentService commentService = new CommentService()) {
            commentService.save(comment);

            LOG.info("{} has commented the post [{}]", user.getNickname(), postId);

            return new ActionResult(REDIRECT_URL + postId, true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
