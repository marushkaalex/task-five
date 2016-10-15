package com.epam.am.whatacat.action.auth;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.get.ShowPageAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowLoginAction extends ShowPageAction {
    public ShowLoginAction() {
        super("login");
    }

    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        if (request.getSession().getAttribute("user") != null) {
            return new ActionResult("/", true);
        }

        String referer = request.getHeader("Referer");
        if (referer != null) {
            String[] split = referer.split("/");
            if (split.length > 3) { // 'http' '' 'localhost'
                request.setAttribute("fromUrl", split[split.length - 1]); // last segment
            }
        }
        return super.execute(request, response);
    }
}
