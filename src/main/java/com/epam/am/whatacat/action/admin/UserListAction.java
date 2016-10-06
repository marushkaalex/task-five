package com.epam.am.whatacat.action.admin;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.model.*;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserListAction implements Action {
    public static final int LIMIT = 20;

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
            String pageParameter = request.getParameter("page");
            int page = 0;
            try {
                page = Integer.parseInt(pageParameter);
            } catch (NumberFormatException ignored) {}
            List<User> all = userService.getAll(LIMIT, LIMIT * page);
            long count = userService.count();

            AdminTable table = new AdminTable();
            table.setTitle("admin.users");

            AdminTable.Row headers = new AdminTable.Row();
            headers
                    .addColumn("admin.users.header.email")
                    .addColumn("admin.users.header.nickname")
                    .addColumn("admin.users.header.role")
                    .addColumn("admin.users.header.gender")
                    .addColumn("admin.users.header.rating")
                    .addColumn("admin.users.header.date");

            table.setHeaders(headers);

            List<AdminTable.Row> rows = new ArrayList<>(all.size());
            for (User item : all) {
                Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
                String formattedDate = new SimpleDateFormat("yyyy-MM-dd", locale).format(item.getRegistrationDate());
                AdminTable.Row row = new AdminTable.Row();
                row
                        .addColumn(item.getEmail())
                        .addColumn(item.getNickname())
                        .addColumn(item.getRole().name())
                        .addColumn(item.getGender().name())
                        .addColumn(String.valueOf(item.getRating()))
                        .addColumn(formattedDate);

                rows.add(row);
            }

            table.setRows(rows);
            table.setPage(page);
            table.setPageCount(Double.valueOf(Math.floor(count / LIMIT)).intValue());

            request.setAttribute("table", table);
            return new ActionResult("admin");
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
