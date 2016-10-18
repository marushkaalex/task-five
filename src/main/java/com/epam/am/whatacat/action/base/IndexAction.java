package com.epam.am.whatacat.action.base;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.model.PaginatedArrayList;
import com.epam.am.whatacat.model.PaginatedList;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.PostService;
import com.epam.am.whatacat.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class IndexAction extends ShowPageAction {

    private static final Logger LOG = LoggerFactory.getLogger(IndexAction.class);

    private static final int POSTS_PER_PAGE = 2;
    private static final int MAX_TEXT_LENGTH = 1000;
    private static final String VIEW = "index";
    private static final String PARAMETER_PAGE = "page";
    private static final String ATTRIBUTE_POST_LIST = "postList";

    public IndexAction() {
        super(VIEW);
    }

    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        try (PostService postService = new PostService()) {
            String page = request.getParameter(PARAMETER_PAGE);
            int pageNumber = page == null || page.isEmpty() ? 1 : Integer.parseInt(page);

            User user = getUser(request);
            List<Post> postList;
            postList = postService.getPostListByStatus(
                    Post.Status.ALLOWED,
                    user == null ? null : user.getId(),
                    POSTS_PER_PAGE,
                    POSTS_PER_PAGE * pageNumber - POSTS_PER_PAGE
            );

            postList.forEach(post -> post.setContent(trim(post.getContent())));
            PaginatedList<Post> paginatedList = new PaginatedArrayList<>(postList);
            paginatedList.setPage(pageNumber);
            double postCount = postService.countByStatus(Post.Status.ALLOWED);
            paginatedList.setPageCount(Double.valueOf(Math.ceil(postCount / POSTS_PER_PAGE)).intValue());
            request.setAttribute(ATTRIBUTE_POST_LIST, paginatedList);

            LOG.info("Index page {} has been shown", page);

            return super.execute(request, response);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }

    private String trim(String origin) {
        if (origin.length() > MAX_TEXT_LENGTH) {
            return origin.substring(0, MAX_TEXT_LENGTH - 1) + '…';
        } else {
            return origin;
        }
    }
}
