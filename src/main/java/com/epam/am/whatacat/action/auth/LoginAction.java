package com.epam.am.whatacat.action.auth;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.service.UserService;
import com.epam.am.whatacat.validation.FormValidator;
import com.epam.am.whatacat.validation.FormValidatorFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class LoginAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
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
                request.setAttribute("errorList", errorMap);
                return new ActionResult("login");
            } else {
                request.getSession().setAttribute("user", user);
            }

//            String referer = request.getHeader("Referer");
//            if (referer != null) {
//                String[] split = referer.split("/do/");
//                if (split.length > 1) {
//                    return new ActionResult(split[1], true);
//                }
//            }
            String fromUrl = request.getParameter("fromUrl");
            return new ActionResult(fromUrl == null || fromUrl.isEmpty() ? "/" : fromUrl, true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}