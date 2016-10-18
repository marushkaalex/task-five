package com.epam.am.whatacat.action.admin;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.BaseAction;
import com.epam.am.whatacat.model.Gender;
import com.epam.am.whatacat.model.Role;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.service.UserService;
import com.epam.am.whatacat.utils.ParameterUtils;
import com.epam.am.whatacat.validation.FormValidator;
import com.epam.am.whatacat.validation.FormValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class EditUserAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(EditUserAction.class);

    private static final String REDIRECT_URL = "/admin/edit-user?id=";
    private static final String VALIDATOR = "edit-user";

    private static final String PARAMETER_GENDER = "gender";
    private static final String PARAMETER_ROLE = "role";
    private static final String PARAMETER_RATING = "rating";
    private static final String PARAMETER_NICKNAME = "nickname";
    private static final String PARAMETER_EMAIL = "email";
    private static final String PARAMETER_SUCCESS = "success";
    private static final String ERROR_RATING = "admin.users.error.rating";
    private static final String ERROR_NICKNAME_ALREADY_TAKEN = "profile.error.nickname.already-taken";
    private static final String ERROR_EMAIL_ALREADY_IN_USE = "profile.error.email-already-in-use";
    private static final String MESSAGE_SUCCESS = "admin.users.changes-saved-successfully";

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        long id = getIdParameter(request);
        if (id == INVALID_ID) {
            return new ActionResult(HttpServletResponse.SC_BAD_REQUEST);
        }

        Gender gender = Gender.valueOf(request.getParameter(PARAMETER_GENDER));
        Role role = Role.valueOf(request.getParameter(PARAMETER_ROLE));

        FormValidator validator = FormValidatorFactory.getInstance().getValidator(VALIDATOR);
        Map<String, String> errorMap = validator.validate(request.getParameterMap());
        if (!errorMap.isEmpty()) {
            setErrorMap(request, errorMap);
            return new ActionResult(REDIRECT_URL + id, true);
        }

        try (UserService userService = new UserService()) {
            User user = userService.findById(id);
            if (user == null) {
                return new ActionResult(REDIRECT_URL + id, true); // not found
            }

            long rating = ParameterUtils.parseLong(request.getParameter(PARAMETER_RATING), Long.MIN_VALUE);
            if (rating == Long.MIN_VALUE) {
                errorMap.put(PARAMETER_RATING, ERROR_RATING);
            }

            String nickname = request.getParameter(PARAMETER_NICKNAME);
            if (!checkNickname(userService, user.getNickname(), nickname)) {
                errorMap.put(PARAMETER_NICKNAME, ERROR_NICKNAME_ALREADY_TAKEN);
            }

            String email = request.getParameter(PARAMETER_EMAIL);
            if (!checkEmail(userService, user.getEmail(), email)) {
                errorMap.put(PARAMETER_EMAIL, ERROR_EMAIL_ALREADY_IN_USE);
            }

            if (errorMap.isEmpty()) {
                user.setNickname(nickname);
                user.setEmail(email);
                user.setGender(gender);
                user.setRole(role);
                user.setRating(rating);
                userService.save(user);
                errorMap.put(PARAMETER_SUCCESS, MESSAGE_SUCCESS);
            }

            setErrorMap(request, errorMap);

            LOG.info("User [{}] has been edited", id);

            return new ActionResult(REDIRECT_URL + id, true);
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
