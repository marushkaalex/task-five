package com.epam.am.whatacat.action.auth;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.model.Gender;
import com.epam.am.whatacat.model.Role;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.service.UserService;
import com.epam.am.whatacat.validation.FormValidator;
import com.epam.am.whatacat.validation.FormValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

public class RegisterAction implements Action {
    private static final Logger log = LoggerFactory.getLogger(RegisterAction.class);

    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        FormValidator validator = FormValidatorFactory.getInstance().getValidator("register");
        Map<String, String> errorMap = validator.validate(request.getParameterMap());
        if (!errorMap.isEmpty()) {
            request.setAttribute("errorMap", errorMap);
            return new ActionResult("register");
        }

        try (UserService userService = new UserService()) {
            String email = request.getParameter("email");
            if (!userService.isEmailFree(email)) {
                errorMap.put("email", "register.error.email.already-in-use");
            }

            String nickname = request.getParameter("nickname");
            if (!userService.isNicknameFree(nickname)) {
                errorMap.put("nickname", "register.error.nickname.already-in-use");
            }
            String password = request.getParameter("password");
            String passwordConfirmed = request.getParameter("confirmPassword");

            if (!password.equals(passwordConfirmed)) {
                errorMap.put("confirmPassword", "register.error.passwords-dont-match");
            }

            if (!errorMap.isEmpty()) {
                request.setAttribute("errorMap", errorMap);
                return new ActionResult("register");
            }

            User user = new User();
            user.setEmail(email);
            user.setNickname(nickname);
            user.setGender(Gender.UNDEFINED);
            user.setRole(Role.USER);
            user.setRegistrationDate(new Date());

            user = userService.register(user, password);
            request.getSession().setAttribute("user", user);

            log.info("User {} <{}> has been registered", nickname, email);
            return new ActionResult("/", true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
