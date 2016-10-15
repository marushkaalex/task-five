package com.epam.am.whatacat.action.base;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.ErrorHandlingAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowPageAction extends ErrorHandlingAction {
    private ActionResult actionResult;

    public ShowPageAction(String page) {
        this.actionResult = new ActionResult(page);
    }

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        return actionResult;
    }
}
