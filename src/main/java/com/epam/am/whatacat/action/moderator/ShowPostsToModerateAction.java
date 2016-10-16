package com.epam.am.whatacat.action.moderator;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.model.AdminTable;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.service.PostService;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.utils.ParameterUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShowPostsToModerateAction implements Action {
    private static final int COLUMN_MAX_LENGTH = 30;
    public static final int POSTS_PER_PAGE = 2;

    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        int page = ParameterUtils.parseInt(request.getParameter("page"), 1);
        try (PostService postService = new PostService()) {
            List<Post> postList = postService.getPostListByStatus(
                    Post.Status.ON_MODERATION,
                    null,
                    POSTS_PER_PAGE,
                    POSTS_PER_PAGE * (page - 1)
            );
            AdminTable table = new AdminTable();
            AdminTable.Row headers = new AdminTable.Row()
                    .addColumn("admin.posts.header.title")
                    .addColumn("admin.posts.header.author")
                    .addColumn("admin.posts.header.date")
                    .addColumn("admin.posts.header.moderate");
            table.setHeaders(headers);
            Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", locale);
            List<AdminTable.Row> rowList = new ArrayList<>();

            for (Post post : postList) {
                String formattedDate = dateFormat.format(post.getPublicationDate());
                AdminTable.Row row = new AdminTable.Row();
                row
                        .addColumn(cutString(post.getTitle()))
                        .addColumn(cutString(post.getAuthor().getNickname()))
                        .addColumn(formattedDate)
                        .addColumn("admin.posts.header.moderate", true, "/post?id=" + post.getId());
                rowList.add(row);
            }

            double count = postService.countByStatus(Post.Status.ON_MODERATION);
            table.setRows(rowList);
            table.setPage(page);
            table.setPageCount(Double.valueOf(Math.ceil(count / POSTS_PER_PAGE)).intValue());
            table.setTitle("admin.posts");

            request.setAttribute("table", table);
            request.setAttribute("type", "moderation");
            return new ActionResult("admin");
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }

    private String cutString(String str) {
        if (str.length() > COLUMN_MAX_LENGTH) {
            str = str.substring(0, COLUMN_MAX_LENGTH - 1) + '…';
        }
        return str;
    }
}
