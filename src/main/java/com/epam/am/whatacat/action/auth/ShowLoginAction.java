package com.epam.am.whatacat.action.auth;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.base.ShowPageAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowLoginAction extends ShowPageAction {

    private static final String VIEW = "login";
    private static final String REDIRECT_URI = "/";

    public ShowLoginAction() {
        super(VIEW);
    }

    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        if (getUser(request) != null) {
            return new ActionResult(REDIRECT_URI, true);
        }
        return super.execute(request, response);
    }
}
