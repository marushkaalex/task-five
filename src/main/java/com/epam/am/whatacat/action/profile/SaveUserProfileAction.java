package com.epam.am.whatacat.action.profile;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.BaseAction;
import com.epam.am.whatacat.model.Gender;
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

public class SaveUserProfileAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(SaveUserProfileAction.class);

    private static final String VALIDATOR = "save-user";

    private static final String REDIRECT_URL = "/profile";
    private static final String PARAMETER_GENDER = "gender";
    private static final String PARAMETER_NICKNAME = "nickname";
    private static final String PARAMETER_EMAIL = "email";

    private static final String ERROR_NICKNAME_ALREADY_TAKEN = "profile.error.nickname.already-taken";
    private static final String ERROR_EMAIL_ALREADY_IN_USE = "profile.error.email-already-in-use";
    private static final String KEY_SUCCESS = "success";
    private static final String MESSAGE_SAVE_SUCCESS = "profile.save.success";

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        User user = getUser(request);
        long id = getIdParameter(request);
        if (user.getId() != id) {
            return new ActionResult(HttpServletResponse.SC_BAD_REQUEST);
        }

        FormValidator validator = FormValidatorFactory.getInstance().getValidator(VALIDATOR);
        Map<String, String> errorMap = validator.validate(request.getParameterMap());
        if (!errorMap.isEmpty()) {
            setErrorMap(request, errorMap);
            return new ActionResult(REDIRECT_URL, true);
        }

        String genderStr = request.getParameter(PARAMETER_GENDER);
        Gender gender = Gender.valueOf(genderStr);

        try (UserService userService = new UserService()) {
            String nickname = request.getParameter(PARAMETER_NICKNAME);
            if (!checkNickname(userService, user.getNickname(), nickname)) {
                errorMap.put(PARAMETER_NICKNAME, ERROR_NICKNAME_ALREADY_TAKEN);
            }

            String email = request.getParameter(PARAMETER_EMAIL);
            if (!checkEmail(userService, user.getEmail(), email)) {
                errorMap.put(PARAMETER_EMAIL, ERROR_EMAIL_ALREADY_IN_USE);
            }

            if (errorMap.isEmpty()) {
                user.setEmail(email);
                user.setNickname(nickname);
                user.setGender(gender);
                userService.save(user);

                errorMap.put(KEY_SUCCESS, MESSAGE_SAVE_SUCCESS);
            }

            setErrorMap(request, errorMap);

            LOG.info("Profile of user [{}] has been save", id);

            return new ActionResult(REDIRECT_URL, true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }

    private boolean checkNickname(UserService userService, String userNickname, String nickname) throws ServiceException {
        return userNickname.equals(nickname) || userService.isNicknameFree(nickname);
    }

    private boolean checkEmail(UserService userService, String userEmail, String email) throws ServiceException {
        return userEmail.equals(email) || userService.isEmailFree(email);
    }
}
