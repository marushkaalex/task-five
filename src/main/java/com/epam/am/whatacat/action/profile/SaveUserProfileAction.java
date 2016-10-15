package com.epam.am.whatacat.action.profile;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.model.Gender;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.service.UserService;
import com.epam.am.whatacat.utils.ParameterUtils;
import com.epam.am.whatacat.validation.FormValidator;
import com.epam.am.whatacat.validation.FormValidatorFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class SaveUserProfileAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        request.getSession().removeAttribute("errorMap");

        User user = (User) request.getSession().getAttribute("user");
        long id = ParameterUtils.parseInt(request.getParameter("id"), -1);
        if (user.getId() != id) {
            return new ActionResult(HttpServletResponse.SC_BAD_REQUEST);
        }

        FormValidator validator = FormValidatorFactory.getInstance().getValidator("save-user");
        Map<String, String> errorMap = validator.validate(request.getParameterMap());
        if (!errorMap.isEmpty()) {
            request.getSession().setAttribute("errorMap", errorMap);
            return new ActionResult("/profile", true);
        }

        String genderStr = request.getParameter("gender");
        Gender gender = Gender.valueOf(genderStr);

        try (UserService userService = new UserService()) {
            String nickname = request.getParameter("nickname");
            if (!checkNickname(userService, user.getNickname(), nickname)) {
                errorMap.put("nickname", "profile.error.nickname.already-taken");
            }

            String email = request.getParameter("email");
            if (!checkEmail(userService, user.getEmail(), email)) {
                errorMap.put("email", "profile.error.email-already-in-use");
            }

            if (errorMap.isEmpty()) {
                user.setEmail(email);
                user.setNickname(nickname);
                user.setGender(gender);
                userService.save(user);

                errorMap.put("success", "profile.save.success");
            }

            request.getSession().setAttribute("errorMap", errorMap);
            return new ActionResult("/profile", true);
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
