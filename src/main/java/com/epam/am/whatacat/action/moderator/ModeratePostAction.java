package com.epam.am.whatacat.action.moderator;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.service.PostService;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.utils.ParameterUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ModeratePostAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        String decision = request.getParameter("decision");
        Post.Status status;
        switch (decision) {
            case "allow":
                status = Post.Status.ALLOWED;
                break;
            case "deny":
                status = Post.Status.DENIED;
                break;
            default:
                return getBadRequest();
        }
        long id = ParameterUtils.parseLong(request.getParameter("id"), -1L);
        if (id == -1) {
            return getBadRequest();
        }

        try (PostService postService = new PostService()) {
            Post post = postService.findById(id);
            if (post == null) {
                return getBadRequest();
            }

            post.setStatus(status);
            postService.save(post);
            return new ActionResult("/post?id=" + id, true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }

    private ActionResult getBadRequest() {
        return new ActionResult(HttpServletResponse.SC_BAD_REQUEST);
    }
}
