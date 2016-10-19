package com.epam.am.whatacat.action.moderator;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.BaseAction;
import com.epam.am.whatacat.model.AdminTable;
import com.epam.am.whatacat.model.Post;
import com.epam.am.whatacat.service.PostService;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.utils.ParameterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShowPostsToModerateAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(ShowPostsToModerateAction.class);

    private static final int POSTS_PER_PAGE = 2;
    private static final int COLUMN_MAX_LENGTH = 30;

    private static final String PARAMETER_PAGE = "page";
    private static final String ATTRIBUTE_TABLE = "table";
    private static final String ATTRIBUTE_TYPE = "type";
    private static final String TYPE = "moderation";
    private static final String VIEW = "admin";
    private static final String REDIRECT_URL = "/post?id=";

    private static final String TABLE_HEADER_TITLE = "admin.posts.header.title";
    private static final String TABLE_HEADER_AUTHOR = "admin.posts.header.author";
    private static final String TABLE_HEADER_DATE = "admin.posts.header.date";
    private static final String TABLE_DATE_FORMAT = "yyyy-MM-dd";
    private static final String TABLE_HEADER_MODERATE = "admin.posts.header.moderate";
    private static final String TABLE_TITLE = "admin.posts";

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        int page = ParameterUtils.parseInt(request.getParameter(PARAMETER_PAGE), 1);
        try (PostService postService = new PostService()) {
            AdminTable table = createTable(postService, page, getLocale(request));

            request.setAttribute(ATTRIBUTE_TABLE, table);
            request.setAttribute(ATTRIBUTE_TYPE, TYPE);

            LOG.info("Post list to moderate page {} has been shown", page);

            return new ActionResult(VIEW);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }

    private AdminTable createTable(PostService postService, int page, Locale locale) throws ServiceException {
        List<Post> postList = postService.getPostListByStatus(
                Post.Status.ON_MODERATION,
                null,
                POSTS_PER_PAGE,
                POSTS_PER_PAGE * (page - 1)
        );

        AdminTable table = new AdminTable();
        AdminTable.Row headers = new AdminTable.Row()
                .addColumn(TABLE_HEADER_TITLE)
                .addColumn(TABLE_HEADER_AUTHOR)
                .addColumn(TABLE_HEADER_DATE)
                .addColumn(TABLE_HEADER_MODERATE);
        table.setHeaders(headers);
        SimpleDateFormat dateFormat = new SimpleDateFormat(TABLE_DATE_FORMAT, locale);
        List<AdminTable.Row> rowList = new ArrayList<>();

        for (Post post : postList) {
            String formattedDate = dateFormat.format(post.getPublicationDate());
            AdminTable.Row row = new AdminTable.Row();
            row
                    .addColumn(cutString(post.getTitle()))
                    .addColumn(cutString(post.getAuthor().getNickname()))
                    .addColumn(formattedDate)
                    .addColumn(TABLE_HEADER_MODERATE, true, REDIRECT_URL + post.getId());
            rowList.add(row);
        }

        double count = postService.countByStatus(Post.Status.ON_MODERATION);
        table.setRows(rowList);
        table.setPage(page);
        table.setPageCount(Double.valueOf(Math.ceil(count / POSTS_PER_PAGE)).intValue());
        table.setTitle(TABLE_TITLE);

        return table;
    }

    private String cutString(String str) {
        if (str.length() > COLUMN_MAX_LENGTH) {
            str = str.substring(0, COLUMN_MAX_LENGTH - 1) + '…';
        }
        return str;
    }
}
