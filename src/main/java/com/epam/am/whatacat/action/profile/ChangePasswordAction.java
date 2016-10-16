package com.epam.am.whatacat.action.profile;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.ErrorHandlingAction;
import com.epam.am.whatacat.action.post.CreatePostAction;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.service.UserService;
import com.epam.am.whatacat.validation.FormValidator;
import com.epam.am.whatacat.validation.FormValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Map;
import java.util.Random;

public class ChangePasswordAction extends ErrorHandlingAction {
    private static final Logger LOG = LoggerFactory.getLogger(CreatePostAction.class);

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        request.getSession().removeAttribute("errorMap");

        FormValidator validator = FormValidatorFactory.getInstance().getValidator("change-password");
        Map<String, String> errorMap = validator.validate(request.getParameterMap());
        if (!errorMap.isEmpty()) {
            request.getSession().setAttribute("errorMap", errorMap);
            return new ActionResult("/profile", true);
        }

        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String retypedPassword = request.getParameter("retypeNewPassword");

        if (!newPassword.equals(retypedPassword)) {
            errorMap.put("retypeNewPassword", "profile.error.retype-new-password.doesnt-match");
            request.getSession().setAttribute("errorMap", errorMap);
            return new ActionResult("/profile", true);
        }

        try (UserService userService = new UserService()) {
            User user = (User) request.getSession().getAttribute("user");
            String saltStr = user.getHashedPassword().split(" ")[1];
            byte[] salt = Base64.getDecoder().decode(saltStr);
            String oldHashedPassword = userService.hashPassword(oldPassword, salt);
            if (!oldHashedPassword.equals(user.getHashedPassword())) {
                errorMap.put("oldPassword", "profile.error.old-password.wrong");
                request.getSession().setAttribute("errorMap", errorMap);
                return new ActionResult("/profile", true);
            }

            salt = new byte[16];
            new Random().nextBytes(salt);
            String newHashedPassword = userService.hashPassword(newPassword, salt);
            user.setHashedPassword(newHashedPassword);
            userService.save(user);
            errorMap.put("successPassword", "profile.password.change-success");
            request.getSession().setAttribute("errorMap", errorMap);

            LOG.info("User [{}] has changed the password", user.getId());

            return new ActionResult("/profile", true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
