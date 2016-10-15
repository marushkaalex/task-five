package com.epam.am.whatacat.action.admin;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.ErrorHandlingAction;
import com.epam.am.whatacat.model.Gender;
import com.epam.am.whatacat.model.Role;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.service.UserService;
import com.epam.am.whatacat.utils.ParameterUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowEditUserAction extends ErrorHandlingAction {
    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        // TODO: 12.10.2016 validate and check
        long id = ParameterUtils.parseLong(request.getParameter("id"), -1L);
        if (id == -1) {
            return new ActionResult(HttpServletResponse.SC_NOT_FOUND);
        }

        try (UserService userService = new UserService()) {

            User user = userService.findById(id);
            request.setAttribute("user", user);
            request.setAttribute("roles", Role.values());
            request.setAttribute("genders", Gender.values());

            return new ActionResult("edit");
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
