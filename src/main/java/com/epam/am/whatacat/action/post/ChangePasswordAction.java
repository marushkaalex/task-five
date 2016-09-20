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

public class ChangePasswordAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        FormValidator validator = FormValidatorFactory.getInstance().getValidator("change-password");
        List<String> errorList = validator.validate(request.getParameterMap());
        if (!errorList.isEmpty()) {
            // TODO: 21.09.2016 show on page
            request.setAttribute("errorList", errorList);
            return new ActionResult("profile");
        }

        String oldPassword = request.getParameter("old");
        String newPassword = request.getParameter("new");

        try (UserService userService = new UserService()) {
            String oldHashedPassword = userService.hashPassword(oldPassword);
            User user = (User) request.getSession().getAttribute("user");
            if (!oldHashedPassword.equals(user.getHashedPassword())) {
                errorList.add("profile.error.old-password.wrong");
                request.setAttribute("errorList", errorList);
                return new ActionResult("profile");
            }

            String newHashedPassword = userService.hashPassword(newPassword);
            user.setHashedPassword(newHashedPassword);
            userService.save(user);
            errorList.add("profile.password.change-success");
            request.setAttribute("errorList", errorList);
            return new ActionResult("profile");
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
