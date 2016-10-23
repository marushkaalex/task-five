package com.epam.am.whatacat.action.admin;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.BaseAction;
import com.epam.am.whatacat.model.AdminTable;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserListAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(UserListAction.class);

    private static final int LIMIT = 2;
    private static final String TYPE = "users";
    private static final String VIEW = "admin";

    private static final String ATTRIBUTE_TABLE = "table";
    private static final String ATTRIBUTE_TYPE = "type";

    private static final String PARAMETER_PAGE = "page";

    private static final String TABLE_TITLE = "admin.users";
    private static final String TABLE_HEADER_ID = "admin.id";
    private static final String TABLE_HEADER_EMAIL = "admin.users.header.email";
    private static final String TABLE__HEADER_NICKNAME = "admin.users.header.nickname";
    private static final String TABLE__HEADER_ROLE = "admin.users.header.role";
    private static final String TABLE_HEADER_GENDER = "admin.users.header.gender";
    private static final String TABLE_HEADER_RATING = "admin.users.header.rating";
    private static final String TABLE_HEADER_DATE = "admin.users.header.date";
    private static final String TABLE_HEADER_EDIT = "admin.edit";
    private static final String TABLE_DATE_FORMAT = "yyyy-MM-dd";
    private static final String TABLE_EDIT_USER_URL = "/admin/edit-user?id=";

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        try (UserService userService = new UserService()) {

            int page = getIntParameter(request, PARAMETER_PAGE, 1);
            List<User> all = userService.getAll(LIMIT, LIMIT * (page - 1));

            Locale locale = getLocale(request);

            long count = userService.count();
            AdminTable table = createTable(all, page, count, locale);
            request.setAttribute(ATTRIBUTE_TABLE, table);
            request.setAttribute(ATTRIBUTE_TYPE, TYPE);

            LOG.info("User list page {} has been shown", page);

            return new ActionResult(VIEW);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }

    private AdminTable createTable(List<User> userList, int page, double count, Locale locale) {
        AdminTable table = new AdminTable();
        table.setTitle(TABLE_TITLE);

        AdminTable.Row headers = new AdminTable.Row();
        headers
                .addColumn(TABLE_HEADER_ID)
                .addColumn(TABLE_HEADER_EMAIL)
                .addColumn(TABLE__HEADER_NICKNAME)
                .addColumn(TABLE__HEADER_ROLE)
                .addColumn(TABLE_HEADER_GENDER)
                .addColumn(TABLE_HEADER_RATING)
                .addColumn(TABLE_HEADER_DATE)
                .addColumn(TABLE_HEADER_EDIT);

        table.setHeaders(headers);

        List<AdminTable.Row> rows = new ArrayList<>(userList.size());
        for (User item : userList) {
            String formattedDate = new SimpleDateFormat(TABLE_DATE_FORMAT, locale).format(item.getRegistrationDate());
            AdminTable.Row row = new AdminTable.Row();
            row
                    .addColumn(String.valueOf(item.getId()))
                    .addColumn(item.getEmail())
                    .addColumn(item.getNickname())
                    .addColumn(item.getRole().getTitleKey(), true)
                    .addColumn(item.getGender().getTitleKey(), true)
                    .addColumn(String.valueOf(item.getRating()))
                    .addColumn(formattedDate)
                    .addColumn(TABLE_HEADER_EDIT, true, TABLE_EDIT_USER_URL + item.getId());

            rows.add(row);
        }

        table.setRows(rows);
        table.setPage(page);

        table.setPageCount(Double.valueOf(Math.ceil(count / LIMIT)).intValue());

        return table;
    }
}
