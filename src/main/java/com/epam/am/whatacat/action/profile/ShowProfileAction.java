package com.epam.am.whatacat.action.profile;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.ErrorHandlingAction;
import com.epam.am.whatacat.model.Gender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowProfileAction extends ErrorHandlingAction {
    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        request.setAttribute("genders", Gender.values());
        return new ActionResult("profile");
    }
}
