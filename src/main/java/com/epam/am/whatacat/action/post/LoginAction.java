package com.epam.am.whatacat.action.post;

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

public class LoginAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        try {
            FormValidator validator = FormValidatorFactory.getInstance().getValidator("login");
            List<String> errorList = validator.validate(request.getParameterMap());
            if (!errorList.isEmpty()) {
                request.setAttribute("errorList", errorList);
                return new ActionResult("login");
            }

            String email = request.getParameter("email");
            String password = request.getParameter("password");

            UserService userService = new UserService();
            User user = userService.logIn(email, password);
            if (user == null) {
                errorList.add("login.error.user-not-found");
                request.setAttribute("errorList", errorList);
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
            return new ActionResult("/", true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
