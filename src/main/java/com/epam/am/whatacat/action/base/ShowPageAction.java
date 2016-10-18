package com.epam.am.whatacat.action.base;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.BaseAction;
import com.epam.am.whatacat.action.post.ShowPostAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowPageAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(ShowPostAction.class);

    private ActionResult actionResult;

    public ShowPageAction(String page) {
        this.actionResult = new ActionResult(page);
    }

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        LOG.info("Page '{}' has been shown", actionResult.getView());
        return actionResult;
    }
}
