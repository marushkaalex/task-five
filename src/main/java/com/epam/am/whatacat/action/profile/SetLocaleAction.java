package com.epam.am.whatacat.action.profile;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SetLocaleAction implements Action {

    private static final Logger LOG = LoggerFactory.getLogger(SetLocaleAction.class);

    private static final String PARAMETER_LOCALE = "locale";
    private static final String LANGUAGE_RU = "ru";
    private static final String LANGUAGE_EN = "en";

    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        String language = request.getParameter(PARAMETER_LOCALE);
        if (language.equals(LANGUAGE_RU) || language.equals(LANGUAGE_EN)) {
            Cookie locale = new Cookie(PARAMETER_LOCALE, language);
            locale.setMaxAge(-1);
            response.addCookie(locale);

            LOG.info("Locale {} has been set", locale);
        }

        return new ActionResult(request.getHeader("Referer"), true);
    }
}
