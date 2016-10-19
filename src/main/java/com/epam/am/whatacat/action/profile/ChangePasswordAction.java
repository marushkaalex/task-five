package com.epam.am.whatacat.action.profile;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.BaseAction;
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

public class ChangePasswordAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(CreatePostAction.class);

    private static final String VALIDATOR = "change-password";
    private static final String REDIRECT_URL = "/profile";

    private static final String PARAMETER_OLD_PASSWORD = "oldPassword";
    private static final String PARAMETER_NEW_PASSWORD = "newPassword";
    private static final String PARAMETER_RETYPE_NEW_PASSWORD = "retypeNewPassword";
    private static final String ERROR_PASSWORD_DOESNT_MATCH = "profile.error.retype-new-password.doesnt-match";
    private static final String ERROR_OLD_PASSWORD_WRONG = "profile.error.old-password.wrong";
    private static final String KEY_SUCCESS_PASSWORD = "successPassword";
    private static final String MESSAGE_PASSWORD_CHANGE_SUCCESS = "profile.password.change-success";

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        request.getSession().removeAttribute(ATTRIBUTE_ERROR_MAP);

        FormValidator validator = FormValidatorFactory.getInstance().getValidator(VALIDATOR);
        Map<String, String> errorMap = validator.validate(request.getParameterMap());
        if (!errorMap.isEmpty()) {
            setErrorMap(request, errorMap);
            return new ActionResult(REDIRECT_URL, true);
        }

        String oldPassword = request.getParameter(PARAMETER_OLD_PASSWORD);
        String newPassword = request.getParameter(PARAMETER_NEW_PASSWORD);
        String retypedPassword = request.getParameter(PARAMETER_RETYPE_NEW_PASSWORD);

        if (!newPassword.equals(retypedPassword)) {
            errorMap.put(PARAMETER_RETYPE_NEW_PASSWORD, ERROR_PASSWORD_DOESNT_MATCH);
            setErrorMap(request, errorMap);
            return new ActionResult(REDIRECT_URL, true);
        }

        try (UserService userService = new UserService()) {
            User user = getUser(request);
            String saltStr = user.getHashedPassword().split(" ")[1];
            byte[] salt = Base64.getDecoder().decode(saltStr);
            String oldHashedPassword = userService.hashPassword(oldPassword, salt);
            if (!oldHashedPassword.equals(user.getHashedPassword())) {
                errorMap.put(PARAMETER_OLD_PASSWORD, ERROR_OLD_PASSWORD_WRONG);
                setErrorMap(request, errorMap);
                return new ActionResult(REDIRECT_URL, true);
            }

            salt = new byte[16];
            new Random().nextBytes(salt);
            String newHashedPassword = userService.hashPassword(newPassword, salt);
            user.setHashedPassword(newHashedPassword);
            userService.save(user);
            errorMap.put(KEY_SUCCESS_PASSWORD, MESSAGE_PASSWORD_CHANGE_SUCCESS);
            setErrorMap(request, errorMap);

            LOG.info("User [{}] has changed the password", user.getId());

            return new ActionResult(REDIRECT_URL, true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }
}
