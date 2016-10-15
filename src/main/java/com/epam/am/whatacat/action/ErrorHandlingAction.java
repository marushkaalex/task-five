package com.epam.am.whatacat.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ErrorHandlingAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        if (request.getSession().getAttribute("errorMapShown") != null) {
            request.getSession().removeAttribute("errorMap");
            request.getSession().removeAttribute("errorMapShown");
        }

        return handle(request, response);
    }

    public abstract ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException;
}
