package com.epam.am.whatacat.action.auth;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.BaseAction;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.service.UserService;
import com.epam.am.whatacat.validation.FormValidator;
import com.epam.am.whatacat.validation.FormValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class LoginAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(LoginAction.class);

    private static final String VALIDATOR = "login";
    private static final String REDIRECT_URL = "login";

    private static final String ATTRIBUTE_USER = "user";

    private static final String PARAMETER_EMAIL = "email";
    private static final String PARAMETER_PASSWORD = "password";
    private static final String PARAMETER_FROM_URL = "fromUrl";

    private static final String KEY_ERROR = "error";
    private static final String ERROR_USER_NOT_FOUND = "login.error.user-not-found";

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        FormValidator validator = FormValidatorFactory.getInstance().getValidator(VALIDATOR);
        Map<String, String> errorMap = validator.validate(request.getParameterMap());
        if (!errorMap.isEmpty()) {
            setErrorMap(request, errorMap);
            return new ActionResult(REDIRECT_URL, true);
        }

        String email = request.getParameter(PARAMETER_EMAIL);
        String password = request.getParameter(PARAMETER_PASSWORD);

        try (UserService userService = new UserService()) {
            User user = userService.logIn(email, password);
            if (user == null) {
                errorMap.put(KEY_ERROR, ERROR_USER_NOT_FOUND);
                setErrorMap(request, errorMap);
                return new ActionResult(REDIRECT_URL);
            } else {
                request.getSession().setAttribute(ATTRIBUTE_USER, user);
            }

            String fromUrl = request.getParameter(PARAMETER_FROM_URL);

            LOG.info("User {} <{}> has been logged in", user.getNickname(), user.getEmail());

            return new ActionResult(fromUrl == null || fromUrl.isEmpty() ? "/" : fromUrl, true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
