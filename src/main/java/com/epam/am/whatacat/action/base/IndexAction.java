package com.epam.am.whatacat.action.base;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.model.PaginatedArrayList;
import com.epam.am.whatacat.model.PaginatedList;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.PostService;
import com.epam.am.whatacat.service.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class IndexAction extends ShowPageAction {
    public static final int POSTS_PER_PAGE = 2;
    public static final int MAX_TEXT_LENGTH = 1000;

    public IndexAction() {
        super("index");
    }

    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        try (PostService postService = new PostService()) {
            String page = request.getParameter("page");
            int pageNumber = page == null || page.isEmpty() ? 1 : Integer.parseInt(page);

            User user = (User) request.getSession().getAttribute("user");
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
            request.setAttribute("postList", paginatedList);
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
