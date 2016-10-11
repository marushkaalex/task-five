package com.epam.am.whatacat.action.profile;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SetLocaleAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        String language = request.getParameter("locale");
        if (language.equals("ru") || language.equals("en")) {
            Cookie locale = new Cookie("locale", language);
            locale.setMaxAge(-1);
            response.addCookie(locale);
        }
        return new ActionResult(request.getHeader("Referer"), true);
    }
}
