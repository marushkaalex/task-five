package com.epam.am.whatacat.action.get;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.dao.DaoException;
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
    public static final int POSTS_PER_PAGE = 10;

    public IndexAction() {
        super("index");
    }

    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        try (PostService postService = new PostService()){
            String page = request.getParameter("page");
            int pageNumber = page == null || page.isEmpty() ? 0 : Integer.parseInt(page);

            User user = (User) request.getSession().getAttribute("user");
            List<Post> postList;
            if (user != null) {
                postList = postService.getPostListWithRating(user.getId(), POSTS_PER_PAGE, POSTS_PER_PAGE * pageNumber);
            } else {
                postList = postService.getPostList(POSTS_PER_PAGE, POSTS_PER_PAGE * pageNumber);
            }

            PaginatedList<Post> paginatedList = new PaginatedArrayList<>(postList);
            paginatedList.setPage(pageNumber);
            request.setAttribute("postList", paginatedList);
            return super.execute(request, response);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
