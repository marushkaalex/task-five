package com.epam.am.whatacat.action.post;

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
import java.util.List;

public class RegisterAction implements Action {
    private static final Logger log = LoggerFactory.getLogger(RegisterAction.class);

    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        FormValidator validator = FormValidatorFactory.getInstance().getValidator("register");
        List<String> errorList = validator.validate(request.getParameterMap());
        if (!errorList.isEmpty()) {
            request.setAttribute("errorList", errorList);
            return new ActionResult("register");
        }

        try (UserService userService = new UserService()) {
            String email = request.getParameter("email");
            if (!userService.isEmailFree(email)) {
                errorList.add("register.error.email-already-in-use");
            }

            String nickname = request.getParameter("nickname");
            if (!userService.isNicknameFree(nickname)) {
                errorList.add("register.error.nickname-already-in-use");
            }
            String password = request.getParameter("password");

            if (!errorList.isEmpty()) {
                request.setAttribute("errorList", errorList);
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
            return new ActionResult("", true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
