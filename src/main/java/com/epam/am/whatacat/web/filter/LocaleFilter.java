package com.epam.am.whatacat.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.Locale;

@WebFilter(filterName = "LocaleFilter", urlPatterns = "/do/*")
public class LocaleFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        Cookie[] cookies = req.getCookies();
        boolean isSet = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("locale")) {
                    Locale locale = new Locale(cookie.getValue());
                    Config.set(req.getSession(), Config.FMT_LOCALE, locale);
                    isSet = true;
                    break;
                }
            }
        }
        if (!isSet) {
            Config.set(req.getSession(), Config.FMT_LOCALE, Locale.ENGLISH);
        }
        filterChain.doFilter(req, resp);
    }

    @Override
    public void destroy() {

    }
}
