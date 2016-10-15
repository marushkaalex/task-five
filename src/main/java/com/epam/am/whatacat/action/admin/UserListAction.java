package com.epam.am.whatacat.action.admin;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.model.AdminTable;
import com.epam.am.whatacat.model.Role;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.service.UserService;
import com.epam.am.whatacat.utils.ParameterUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserListAction implements Action {
    public static final int LIMIT = 2;

    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        try (UserService userService = new UserService()) {
            User user = (User) request.getSession().getAttribute("user");
            if (user == null || user.getRole() != Role.ADMIN) {
                List<String> errorList = new ArrayList<>();
                errorList.add("admin.not-allowed");
                request.setAttribute("errorList", errorList);
                return new ActionResult("admin");
            }
            int page = ParameterUtils.parseInt(request.getParameter("page"), 1);
            List<User> all = userService.getAll(LIMIT, LIMIT * (page - 1));


            Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);

            long count = userService.count();
            AdminTable table = createTable(all, page, count, locale);
            request.setAttribute("table", table);
            return new ActionResult("admin");
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }

    private AdminTable createTable(List<User> userList, int page, double count, Locale locale) {
        AdminTable table = new AdminTable();
        table.setTitle("admin.users");

        AdminTable.Row headers = new AdminTable.Row();
        headers
                .addColumn("admin.id")
                .addColumn("admin.users.header.email")
                .addColumn("admin.users.header.nickname")
                .addColumn("admin.users.header.role")
                .addColumn("admin.users.header.gender")
                .addColumn("admin.users.header.rating")
                .addColumn("admin.users.header.date")
                .addColumn("admin.edit");

        table.setHeaders(headers);

        List<AdminTable.Row> rows = new ArrayList<>(userList.size());
        for (User item : userList) {
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd", locale).format(item.getRegistrationDate());
            AdminTable.Row row = new AdminTable.Row();
            row
                    .addColumn(String.valueOf(item.getId()))
                    .addColumn(item.getEmail())
                    .addColumn(item.getNickname())
                    .addColumn(item.getRole().getTitleKey(), true)
                    .addColumn(item.getGender().getTitleKey(), true)
                    .addColumn(String.valueOf(item.getRating()))
                    .addColumn(formattedDate)
                    .addColumn("admin.edit", true, "admin/edit-user?id=" + item.getId());

            rows.add(row);
        }

        table.setRows(rows);
        table.setPage(page);

        table.setPageCount(Double.valueOf(Math.ceil(count / LIMIT)).intValue());

        return table;
    }
}
