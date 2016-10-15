package com.epam.am.whatacat.action.profile;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.model.User;
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

        // TODO: 15.10.2016 save
        // TODO: 15.10.2016 role

        errorMap.put("success", "profile.save.success");
        request.getSession().setAttribute("errorMap", errorMap);
        return new ActionResult("/profile", true);
    }
}
