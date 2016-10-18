package com.epam.am.whatacat.action.admin;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.model.AdminTable;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.service.PostService;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.utils.ParameterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostListAction implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(PostListAction.class);

    private static final int LIMIT = 2;
    private static final String TYPE = "posts";
    private static final String VIEW = "admin";

    private static final String ATTRIBUTE_TABLE = "table";
    private static final String ATTRIBUTE_TYPE = "type";

    private static final String PARAMETER_PAGE = "page";

    private static final String TABLE_TITLE = "admin.posts";
    private static final String TABLE_HEADER_ID = "admin.id";
    private static final String TABLE_HEADER_TITLE = "admin.posts.header.title";
    private static final String TABLE_HEADER_AUTHOR = "admin.posts.header.author";
    private static final String TABLE_HEADER_RATING = "admin.posts.header.rating";
    private static final String TABLE_HEADER_DATE = "admin.posts.header.date";
    private static final String TABLE_HEADER_STATUS = "admin.posts.header.status";
    private static final String TABLE_HEADER_EDIT = "admin.edit";
    private static final String TABLE_DATE_FORMAT = "yyyy-MM-dd";
    private static final String TABLE_USER_URL = "/user?id=";
    private static final String TABLE_EDIT_POST_URL = "/admin/edit-post?id=";

    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        try (PostService postService = new PostService()) {
            int page = ParameterUtils.parseInt(request.getParameter(PARAMETER_PAGE), 1);
            List<Post> all = postService.getAll(LIMIT, LIMIT * (page - 1));

            Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);

            long count = postService.count();
            AdminTable table = createTable(all, page, count, locale);
            request.setAttribute(ATTRIBUTE_TABLE, table);
            request.setAttribute(ATTRIBUTE_TYPE, TYPE);

            LOG.info("Post list for page {} has been shown", page);

            return new ActionResult(VIEW);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }

    private AdminTable createTable(List<Post> postList, int page, double count, Locale locale) {
        AdminTable table = new AdminTable();
        table.setTitle(TABLE_TITLE);

        AdminTable.Row headers = new AdminTable.Row();
        headers
                .addColumn(TABLE_HEADER_ID)
                .addColumn(TABLE_HEADER_TITLE)
                .addColumn(TABLE_HEADER_AUTHOR)
                .addColumn(TABLE_HEADER_RATING)
                .addColumn(TABLE_HEADER_DATE)
                .addColumn(TABLE_HEADER_STATUS)
                .addColumn(TABLE_HEADER_EDIT);

        table.setHeaders(headers);

        List<AdminTable.Row> rows = new ArrayList<>(postList.size());
        for (Post item : postList) {
            String formattedDate = new SimpleDateFormat(TABLE_DATE_FORMAT, locale).format(item.getPublicationDate());
            AdminTable.Row row = new AdminTable.Row();
            row
                    .addColumn(String.valueOf(item.getId()))
                    .addColumn(item.getTitle())
                    .addColumn(item.getAuthor().getNickname(), TABLE_USER_URL + item.getAuthorId())
                    .addColumn(String.valueOf(item.getRating()))
                    .addColumn(formattedDate)
                    .addColumn(item.getStatus().getTitleKey(), true)
                    .addColumn(TABLE_HEADER_EDIT, true, TABLE_EDIT_POST_URL + item.getId());

            rows.add(row);
        }

        table.setRows(rows);
        table.setPage(page);

        table.setPageCount(Double.valueOf(Math.ceil(count / LIMIT)).intValue());

        return table;
    }
}
