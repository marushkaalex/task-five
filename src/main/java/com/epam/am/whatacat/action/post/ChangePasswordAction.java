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
import java.util.Map;

public class ChangePasswordAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        FormValidator validator = FormValidatorFactory.getInstance().getValidator("change-password");
        Map<String, String> errorMap = validator.validate(request.getParameterMap());
        if (!errorMap.isEmpty()) {
            // TODO: 21.09.2016 show on page
            request.setAttribute("errorMap", errorMap);
            return new ActionResult("profile");
        }

        String oldPassword = request.getParameter("old");
        String newPassword = request.getParameter("new");

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
            errorMap.put("success", "profile.password.change-success");
            request.setAttribute("errorMap", errorMap);
            return new ActionResult("profile");
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
