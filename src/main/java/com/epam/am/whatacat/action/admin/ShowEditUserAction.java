package com.epam.am.whatacat.action.admin;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.service.UserService;
import com.epam.am.whatacat.utils.ParameterUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowEditUserAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        // TODO: 12.10.2016 validate and check
        long id = ParameterUtils.parseLong(request.getParameter("id"), -1L);
        try (UserService userService = new UserService()) {
            User user = userService.findById(id);
            request.setAttribute("user", user);
            return new ActionResult("edit");
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
