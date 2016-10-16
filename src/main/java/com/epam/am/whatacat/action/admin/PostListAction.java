package com.epam.am.whatacat.action.admin;

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

public class PostListAction implements Action {
    public static final int LIMIT = 2;

    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        try (PostService postService = new PostService()) {
            int page = ParameterUtils.parseInt(request.getParameter("page"), 1);
            List<Post> all = postService.getAll(LIMIT, LIMIT * (page - 1));

            Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);

            long count = postService.count();
            AdminTable table = createTable(all, page, count, locale);
            request.setAttribute("table", table);
            request.setAttribute("type", "posts");
            return new ActionResult("admin");
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }

    private AdminTable createTable(List<Post> postList, int page, double count, Locale locale) {
        AdminTable table = new AdminTable();
        table.setTitle("admin.posts");

        AdminTable.Row headers = new AdminTable.Row();
        headers
                .addColumn("admin.id")
                .addColumn("admin.posts.header.title")
                .addColumn("admin.posts.header.author")
                .addColumn("admin.posts.header.rating")
                .addColumn("admin.posts.header.date")
                .addColumn("admin.posts.header.status")
                .addColumn("admin.edit");

        table.setHeaders(headers);

        List<AdminTable.Row> rows = new ArrayList<>(postList.size());
        for (Post item : postList) {
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd", locale).format(item.getPublicationDate());
            AdminTable.Row row = new AdminTable.Row();
            row
                    .addColumn(String.valueOf(item.getId()))
                    .addColumn(item.getTitle())
                    .addColumn(item.getAuthor().getNickname(), "/user?id=" + item.getAuthorId())
                    .addColumn(String.valueOf(item.getRating()))
                    .addColumn(formattedDate)
                    .addColumn(item.getStatus().getTitleKey(), true)
                    .addColumn("admin.edit", true, "/admin/edit-post?id=" + item.getId());

            rows.add(row);
        }

        table.setRows(rows);
        table.setPage(page);

        table.setPageCount(Double.valueOf(Math.ceil(count / LIMIT)).intValue());

        return table;
    }
}
