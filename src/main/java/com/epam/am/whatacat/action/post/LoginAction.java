package com.epam.am.whatacat.action.post;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginAction implements Action {
    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        request.getSession().setAttribute("login", login);
        String referer = request.getHeader("Referer");
        if (referer != null) {
            String[] split = referer.split("/do/");
            if (split.length > 1) {
                return new ActionResult(split[1], true);
            }
        }
        return new ActionResult("index", true);
    }
}