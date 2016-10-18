package com.epam.am.whatacat.action.base;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.BaseAction;
import com.epam.am.whatacat.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(LogoutAction.class);

    private static final String REDIRECT_URL = "/";

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        User user = getUser(request);
        request.getSession().invalidate();

        LOG.info("User {} logged out", user.getNickname());

        return new ActionResult(REDIRECT_URL, true);
    }
}
