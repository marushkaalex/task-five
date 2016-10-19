package com.epam.am.whatacat.action.profile;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.BaseAction;
import com.epam.am.whatacat.model.AdminTable;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.model.Role;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.PostService;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.service.UserService;
import com.epam.am.whatacat.utils.ParameterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class ShowUserAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(ShowUserAction.class);

    private static final int POSTS_PER_PAGE = 2;
    private static final String PARAMETER_PAGE = "page";
    private static final String ATTRIBUTE_USER = "user";
    private static final String ATTRIBUTE_POST_LIST = "postList";
    private static final String VIEW = "user";
    private static final String TABLE_POST_URL = "/post?id=";

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        long id = getIdParameter(request);
        if (id != INVALID_ID) {
            try (
                    UserService userService = new UserService();
                    PostService postService = new PostService()
            ) {
                int page = ParameterUtils.parseInt(request.getParameter(PARAMETER_PAGE), 1);
                User user = userService.findById(id);
                if (user == null) return new ActionResult(HttpServletResponse.SC_NOT_FOUND);

                request.setAttribute(ATTRIBUTE_USER, user);

                AdminTable userPosts = getUserPosts(request, id, postService, page);
                request.setAttribute(ATTRIBUTE_POST_LIST, userPosts);

                LOG.info("User [{}] has been shown", id);

                return new ActionResult(VIEW);
            } catch (ServiceException e) {
                throw new ActionException(e);
            }
        } else {
            return new ActionResult(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private AdminTable getUserPosts(HttpServletRequest request, long userId, PostService postService, int page) throws ServiceException {
        User user = getUser(request);
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

        double count = postService.countUsersPosts(userId, status);

        AdminTable adminTable = new AdminTable();
        List<AdminTable.Row> rowList = new ArrayList<>();

        for (Post post : allOfUser) {
            AdminTable.Row row = new AdminTable.Row()
                    .addColumn(String.valueOf(post.getRating()))
                    .addColumn(post.getTitle(), TABLE_POST_URL + post.getId());

            if (showStatus) {
                row.addColumn(post.getStatus().getTitleKey(), true, null);
            }

            rowList.add(row);
        }

        adminTable.setRows(rowList);
        adminTable.setPage(page);
        adminTable.setPageCount(Double.valueOf(Math.ceil(count / POSTS_PER_PAGE)).intValue());

        return adminTable;
    }
}
