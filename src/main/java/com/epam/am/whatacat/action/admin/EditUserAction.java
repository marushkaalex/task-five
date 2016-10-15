package com.epam.am.whatacat.action.admin;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.ErrorHandlingAction;
import com.epam.am.whatacat.model.Gender;
import com.epam.am.whatacat.model.Role;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.service.UserService;
import com.epam.am.whatacat.utils.ParameterUtils;
import com.epam.am.whatacat.validation.FormValidator;
import com.epam.am.whatacat.validation.FormValidatorFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class EditUserAction extends ErrorHandlingAction {
    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        long id = ParameterUtils.parseLong(request.getParameter("id"), -1L);
        if (id == -1) {
            return new ActionResult(HttpServletResponse.SC_BAD_REQUEST);
        }

        Gender gender = Gender.valueOf(request.getParameter("gender"));
        Role role = Role.valueOf(request.getParameter("role"));

        FormValidator validator = FormValidatorFactory.getInstance().getValidator("edit-user");
        Map<String, String> errorMap = validator.validate(request.getParameterMap());
        if (!errorMap.isEmpty()) {
            request.getSession().setAttribute("errorMap", errorMap);
            return new ActionResult("/admin/edit-user?id=" + id, true);
        }

        try (UserService userService = new UserService()) {
            User user = userService.findById(id);
            if (user == null) {
                return new ActionResult("/admin/edit-user?id=" + id, true); // not found
            }

            long rating = ParameterUtils.parseLong(request.getParameter("rating"), Long.MIN_VALUE);
            if (rating == Long.MIN_VALUE) {
                errorMap.put("rating", "admin.users.error.rating");
            }

            String nickname = request.getParameter("nickname");
            if (!checkNickname(userService, user.getNickname(), nickname)) {
                errorMap.put("nickname", "profile.error.nickname.already-taken");
            }

            String email = request.getParameter("email");
            if (!checkEmail(userService, user.getEmail(), email)) {
                errorMap.put("email", "profile.error.email-already-in-use");
            }

            if (errorMap.isEmpty()) {
                user.setNickname(nickname);
                user.setEmail(email);
                user.setGender(gender);
                user.setRole(role);
                user.setRating(rating);
                userService.save(user);
                errorMap.put("success", "admin.users.changes-saved-successfully");
            }

            request.getSession().setAttribute("errorMap", errorMap);
            return new ActionResult("/admin/edit-user?id=" + id, true);
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
