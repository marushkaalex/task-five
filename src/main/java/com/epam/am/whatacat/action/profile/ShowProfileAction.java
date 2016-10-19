package com.epam.am.whatacat.action.profile;

import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.action.BaseAction;
import com.epam.am.whatacat.model.Gender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowProfileAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(ShowProfileAction.class);

    private static final String VIEW = "profile";
    private static final String ATTRIBUTE_GENDERS = "genders";

    @Override
    public ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        request.setAttribute(ATTRIBUTE_GENDERS, Gender.values());

        LOG.info("Profile has been shown");

        return new ActionResult(VIEW);
    }
}
