package com.epam.am.whatacat.action.auth;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.ErrorHandlingAction;
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

public class RegisterAction extends ErrorHandlingAction {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterAction.class);

    private static final String VIEW = "register";
    private static final String VALIDATOR = "register";
    private static final String REDIRECT_URL = "/";

    private static final String PARAMETER_EMAIL = "email";
    private static final String ERROR_EMAIL_ALREADY_IN_USE = "register.error.email.already-in-use";
    private static final String PARAMETER_NICKNAME = "nickname";
    private static final String PARAMETER_PASSWORD = "password";
    private static final String PARAMETER_CONFIRM_PASSWORD = "confirmPassword";

    private static final String ERROR_NICKNAME_ALREADY_IN_USE = "register.error.nickname.already-in-use";
    private static final String ERROR_PASSWORDS_DONT_MATCH = "register.error.passwords-dont-match";

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        FormValidator validator = FormValidatorFactory.getInstance().getValidator(VALIDATOR);
        Map<String, String> errorMap = validator.validate(request.getParameterMap());
        if (!errorMap.isEmpty()) {
            setErrorMap(request, errorMap);
            return new ActionResult(VIEW);
        }

        try (UserService userService = new UserService()) {
            String email = request.getParameter(PARAMETER_EMAIL);
            if (!userService.isEmailFree(email)) {
                errorMap.put(PARAMETER_EMAIL, ERROR_EMAIL_ALREADY_IN_USE);
            }

            String nickname = request.getParameter(PARAMETER_NICKNAME);
            if (!userService.isNicknameFree(nickname)) {
                errorMap.put(PARAMETER_NICKNAME, ERROR_NICKNAME_ALREADY_IN_USE);
            }
            String password = request.getParameter(PARAMETER_PASSWORD);
            String passwordConfirmed = request.getParameter(PARAMETER_CONFIRM_PASSWORD);

            if (!password.equals(passwordConfirmed)) {
                errorMap.put(PARAMETER_CONFIRM_PASSWORD, ERROR_PASSWORDS_DONT_MATCH);
            }

            if (!errorMap.isEmpty()) {
                setErrorMap(request, errorMap);
                return new ActionResult(VIEW);
            }

            User user = new User();
            user.setEmail(email);
            user.setNickname(nickname);
            user.setGender(Gender.UNDEFINED);
            user.setRole(Role.USER);
            user.setRegistrationDate(new Date());

            user = userService.register(user, password);
            request.getSession().setAttribute(ATTRIBUTE_USER, user);

            LOG.info("User {} <{}> has been registered", nickname, email);

            return new ActionResult(REDIRECT_URL, true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
