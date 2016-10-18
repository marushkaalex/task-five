package com.epam.am.whatacat.action.admin;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.ErrorHandlingAction;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.service.PostService;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.utils.ParameterUtils;
import com.epam.am.whatacat.validation.FormValidator;
import com.epam.am.whatacat.validation.FormValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class EditPostAction extends ErrorHandlingAction {
    private static final Logger LOG = LoggerFactory.getLogger(EditPostAction.class);

    private static final String VALIDATOR = "post";
    private static final String REDIRECT_URL = "/admin/edit-post?id=";

    private static final String PARAMETER_STATUS = "status";
    private static final String PARAMETER_RATING = "rating";
    private static final String PARAMETER_TITLE = "title";
    private static final String PARAMETER_CONTENT = "content";
    private static final String PARAMETER_SUCCESS = "success";

    private static final String ERROR_RATING = "admin.posts.error.rating";
    private static final String MESSAGE_SUCCESS = "admin.posts.changes-saved-successfully";

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        long id = getIdParameter(request);
        if (id == INVALID_ID) {
            return new ActionResult(HttpServletResponse.SC_BAD_REQUEST);
        }

        Post.Status status = Post.Status.valueOf(request.getParameter(PARAMETER_STATUS));

        FormValidator validator = FormValidatorFactory.getInstance().getValidator(VALIDATOR);
        Map<String, String> errorMap = validator.validate(request.getParameterMap());

        long rating = ParameterUtils.parseLong(request.getParameter(PARAMETER_RATING), Long.MIN_VALUE);
        if (rating == Long.MIN_VALUE) {
            errorMap.put(PARAMETER_RATING, ERROR_RATING);
        }

        if (!errorMap.isEmpty()) {
            setErrorMap(request, errorMap);
            return new ActionResult(REDIRECT_URL + id, true);
        }

        try (PostService postService = new PostService()) {
            Post post = postService.findById(id);
            String title = request.getParameter(PARAMETER_TITLE);
            String content = request.getParameter(PARAMETER_CONTENT);
            post.setTitle(title);
            post.setContent(content);
            post.setStatus(status);
            post.setRating(rating);
            postService.save(post);

            errorMap.put(PARAMETER_SUCCESS, MESSAGE_SUCCESS);
            setErrorMap(request, errorMap);

            LOG.info("Post [{}] has been edited", id);

            return new ActionResult(REDIRECT_URL + id, true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
