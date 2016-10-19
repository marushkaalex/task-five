package com.epam.am.whatacat.action.post;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.BaseAction;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.PostService;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.validation.FormValidator;
import com.epam.am.whatacat.validation.FormValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class CreatePostAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(CreatePostAction.class);

    private static final String VALIDATOR = "post";
    private static final String VIEW = "create-post";
    private static final String REDIRECT_URL = "post?id=";
    private static final String PARAMETER_TITLE = "title";
    private static final String PARAMETER_CONTENT = "content";

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        FormValidator validator = FormValidatorFactory.getInstance().getValidator(VALIDATOR);
        Map<String, String> errorMap = validator.validate(request.getParameterMap());
        if (!errorMap.isEmpty()) {
            setErrorMap(request, errorMap);
            return new ActionResult(VIEW);
        }

        String title = request.getParameter(PARAMETER_TITLE);
        String content = request.getParameter(PARAMETER_CONTENT);

        try (PostService postService = new PostService()) {
            User user = getUser(request);
            Post post = postService.createPost(Post.TYPE_TEXT, title, content, user.getId());

            LOG.info("Post [{}] has been created", post.getId());

            return new ActionResult(REDIRECT_URL + post.getId(), true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
