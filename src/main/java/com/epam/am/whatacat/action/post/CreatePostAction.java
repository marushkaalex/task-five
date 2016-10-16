package com.epam.am.whatacat.action.post;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
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

public class CreatePostAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(CreatePostAction.class);

    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        FormValidator validator = FormValidatorFactory.getInstance().getValidator("post");
        Map<String, String> errorMap = validator.validate(request.getParameterMap());
        if (!errorMap.isEmpty()) {
            request.setAttribute("errorMap", errorMap);
            return new ActionResult("create-post");
        }

        String title = request.getParameter("title");
        String content = request.getParameter("content");

        try (PostService postService = new PostService()) {
            User user = (User) request.getSession().getAttribute("user");
            Post post = postService.createPost(Post.TYPE_TEXT, title, content, user.getId());

            LOG.info("Post [{}] has been created", post.getId());
            return new ActionResult("post?id=" + post.getId(), true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
