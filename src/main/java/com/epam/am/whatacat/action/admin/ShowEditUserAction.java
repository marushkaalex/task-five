package com.epam.am.whatacat.action.admin;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.ErrorHandlingAction;
import com.epam.am.whatacat.model.Gender;
import com.epam.am.whatacat.model.Role;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowEditUserAction extends ErrorHandlingAction {

    private static final Logger LOG = LoggerFactory.getLogger(ShowEditUserAction.class);

    private static final String VIEW = "edit-user";

    private static final String ATTRIBUTE_USER = "user";
    private static final String ATTRIBUTE_ROLES = "roles";
    private static final String ATTRIBUTE_GENDERS = "genders";

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        long id = getIdParameter(request);
        if (id == INVALID_ID) {
            return new ActionResult(HttpServletResponse.SC_NOT_FOUND);
        }

        try (UserService userService = new UserService()) {

            User user = userService.findById(id);
            request.setAttribute(ATTRIBUTE_USER, user);
            request.setAttribute(ATTRIBUTE_ROLES, Role.values());
            request.setAttribute(ATTRIBUTE_GENDERS, Gender.values());

            LOG.info("Edit user form has been shown for user [{}]", id);

            return new ActionResult(VIEW);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
