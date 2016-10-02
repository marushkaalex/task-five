package com.epam.am.whatacat.action.admin;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.model.PaginatedArrayList;
import com.epam.am.whatacat.model.PaginatedList;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class UserListAction implements Action {
    public static final int LIMIT = 20;

    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        try (UserService userService = new UserService()) {
            String pageParameter = request.getParameter("page");
            int page = 0;
            try {
                page = Integer.parseInt(pageParameter);
            } catch (NumberFormatException ignored) {}
            List<User> all = userService.getAll(LIMIT, LIMIT * page);
            PaginatedList<User> res = new PaginatedArrayList<>(all);
            res.setPage(page);
            long count = userService.count();
            res.setPageCount(((int) Math.floor(count / LIMIT)));
            request.setAttribute("items", res);
            return new ActionResult("admin");
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
