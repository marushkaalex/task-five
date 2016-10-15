package com.epam.am.whatacat.action.profile;

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

public class ChangePasswordAction extends ErrorHandlingAction {
    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        request.getSession().removeAttribute("errorMap");

        FormValidator validator = FormValidatorFactory.getInstance().getValidator("change-password");
        Map<String, String> errorMap = validator.validate(request.getParameterMap());
        if (!errorMap.isEmpty()) {
            request.getSession().setAttribute("errorMap", errorMap);
            return new ActionResult("profile", true);
        }

        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String retypedPassword = request.getParameter("retypeNewPassword");

        if (!newPassword.equals(retypedPassword)) {
            errorMap.put("retypeNewPassword", "profile.error.retype-new-password.doesnt-match");
            request.getSession().setAttribute("errorMap", errorMap);
            return new ActionResult("profile", true);
        }

        try (UserService userService = new UserService()) {
            String oldHashedPassword = userService.hashPassword(oldPassword);
            User user = (User) request.getSession().getAttribute("user");
            if (!oldHashedPassword.equals(user.getHashedPassword())) {
                errorMap.put("old", "profile.error.old-password.wrong");
                request.setAttribute("errorMap", errorMap);
                return new ActionResult("profile");
            }

            String newHashedPassword = userService.hashPassword(newPassword);
            user.setHashedPassword(newHashedPassword);
            userService.save(user);
            errorMap.put("successPassword", "profile.password.change-success");
            request.setAttribute("errorMap", errorMap);
            return new ActionResult("profile");
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
