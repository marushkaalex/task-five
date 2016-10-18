package com.epam.am.whatacat.action.admin;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.ErrorHandlingAction;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteUserAction extends ErrorHandlingAction {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteUserAction.class);

    private static final String REDIRECT_URL = "/admin/users";

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        long id = getIdParameter(request);
        if (id == INVALID_ID) {
            return new ActionResult(HttpServletResponse.SC_BAD_REQUEST);
        }

        try (UserService userService = new UserService()) {
            userService.delete(id);

            LOG.info("User [{}] has been deleted", id);

            return new ActionResult(REDIRECT_URL, true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
