package com.epam.am.whatacat.action.profile;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.model.AdminTable;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.model.Role;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.PostService;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.service.UserService;
import com.epam.am.whatacat.utils.ParameterUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class ShowUserAction implements Action {
    public static final int POSTS_PER_PAGE = 2;

    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        long id = ParameterUtils.parseLong(request.getParameter("id"), -1L);
        int page = ParameterUtils.parseInt(request.getParameter("page"), 1);
        if (id != -1) {
            try (
                    UserService userService = new UserService();
                    PostService postService = new PostService();
            ) {
                User user = userService.findById(id);
                if (user == null) return new ActionResult(HttpServletResponse.SC_NOT_FOUND);

                request.setAttribute("user", user);

                AdminTable userPosts = getUserPosts(request, id, postService, page);
                request.setAttribute("postList", userPosts);

                return new ActionResult("user");
            } catch (ServiceException e) {
                throw new ActionException(e);
            }
        } else {
            return new ActionResult(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private AdminTable getUserPosts(HttpServletRequest request, long userId, PostService postService, int page) throws ServiceException {
        User user = (User) request.getSession().getAttribute("user");
        // only user, moderators and admins are allowed to see posts of any status
        Post.Status status = null;
        boolean showStatus = true;
        if (user == null || (user.getId() != userId && user.getRole() != Role.MODERATOR && user.getRole() != Role.ADMIN)) {
            status = Post.Status.ALLOWED;
            showStatus = false;
        }
        List<Post> allOfUser = postService.getAllOfUser(
                userId,
                status == null ? null : status.getId(),
                POSTS_PER_PAGE,
                POSTS_PER_PAGE * (page - 1)
        );
        long count = status == null ? postService.count() : postService.countByStatus(status);

        AdminTable adminTable = new AdminTable();
        List<AdminTable.Row> rowList = new ArrayList<>();

        for (Post post : allOfUser) {
            AdminTable.Row row = new AdminTable.Row()
                    .addColumn(String.valueOf(post.getRating()))
                    .addColumn(post.getTitle(), "/post?id=" + post.getId());

            if (showStatus) {
                row.addColumn(getStatusKey(post.getStatus()), true, null);
            }

            rowList.add(row);
        }

        adminTable.setRows(rowList);
        adminTable.setPage(page);
        adminTable.setPageCount(Double.valueOf(Math.floor(count / POSTS_PER_PAGE)).intValue());

        return adminTable;
    }

    private String getStatusKey(Post.Status status) {
        switch (status) {
            case ON_MODERATION:
                return "post.status.on-moderation";
            case ALLOWED:
                return "post.status.allowed";
            case DENIED:
                return "post.status.denied";
            default:
                throw new IllegalArgumentException();
        }
    }
}
