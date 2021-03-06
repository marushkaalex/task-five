package com.epam.am.whatacat.action;

import com.epam.am.whatacat.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import java.util.Locale;
import java.util.Map;

public abstract class BaseAction implements Action {
    public static final String ATTRIBUTE_ERROR_MAP = "errorMap";
    public static final String ATTRIBUTE_ERROR_MAP_SHOWN = "errorMapShown";

    protected static final String PARAMETER_ID = "id";
    protected static final long INVALID_ID = -1L;
    protected static final String ATTRIBUTE_USER = "user";

    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        if (request.getSession().getAttribute(ATTRIBUTE_ERROR_MAP_SHOWN) != null) {
            request.getSession().removeAttribute(ATTRIBUTE_ERROR_MAP);
            request.getSession().removeAttribute(ATTRIBUTE_ERROR_MAP_SHOWN);
        }

        return handle(request, response);
    }

    protected void setErrorMap(HttpServletRequest request, Map<String, String> errorMap) {
        request.getSession().setAttribute(ATTRIBUTE_ERROR_MAP, errorMap);
    }

    protected long getIdParameter(HttpServletRequest request) {
        return getLongParameter(request, PARAMETER_ID, INVALID_ID);
    }

    protected User getUser(HttpServletRequest request) {
        return ((User) request.getSession().getAttribute(ATTRIBUTE_USER));
    }

    protected Locale getLocale(HttpServletRequest request) {
        return (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
    }

    protected Long getLongParameter(HttpServletRequest request, String parameter, Long defaultValue) {
        try {
            return Long.parseLong(request.getParameter(parameter));
        } catch (NumberFormatException ignored) {
            return defaultValue;
        }
    }

    protected Integer getIntParameter(HttpServletRequest request, String parameter, Integer defaultValue) {
        try {
            return Integer.parseInt(request.getParameter(parameter));
        } catch (NumberFormatException ignored) {
            return defaultValue;
        }
    }

    public abstract ActionResult handle(HttpServletRequest request, HttpServletResponse response) throws ActionException;
}
