package com.epam.am.whatacat.action.auth;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.ErrorHandlingAction;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.service.UserService;
import com.epam.am.whatacat.validation.FormValidator;
import com.epam.am.whatacat.validation.FormValidatorFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class LoginAction extends ErrorHandlingAction {
    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        FormValidator validator = FormValidatorFactory.getInstance().getValidator("login");
        Map<String, String> errorMap = validator.validate(request.getParameterMap());
        if (!errorMap.isEmpty()) {
            request.getSession().setAttribute("errorMap", errorMap);
            return new ActionResult("login", true);
        }

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (UserService userService = new UserService()) {
            User user = userService.logIn(email, password);
            if (user == null) {
                errorMap.put("error", "login.error.user-not-found");
                request.getSession().setAttribute("errorMap", errorMap);
                return new ActionResult("login");
            } else {
                request.getSession().setAttribute("user", user);
            }

            String fromUrl = request.getParameter("fromUrl");
            return new ActionResult(fromUrl == null || fromUrl.isEmpty() ? "/" : fromUrl, true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
