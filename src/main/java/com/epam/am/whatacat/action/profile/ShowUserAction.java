package com.epam.am.whatacat.action.profile;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.get.ShowPageAction;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowUserAction extends ShowPageAction {
    public ShowUserAction() {
        super("user");
    }

    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        String idParameter = request.getParameter("id");
        Long id = null;
        if (idParameter != null && !idParameter.isEmpty()) {
            try {
                id = Long.parseLong(idParameter);
                try (UserService userService = new UserService()) {
                    User user = userService.findById(id);
                    request.setAttribute("user", user);
                } catch (ServiceException e) {
                    throw new ActionException(e);
                }
            } catch (NumberFormatException ignored) {}
        }
        return super.execute(request, response);
    }
}
