package com.epam.am.whatacat.servlet;

import com.epam.am.whatacat.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@WebFilter(filterName = "ForwardFilter")
public class ForwardFilter implements Filter {
    private Set<String> availableUrls = new HashSet<>();

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if (req instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) req;
            String requestURI = httpRequest.getRequestURI();
            if (requestURI.startsWith("/static")
                    || requestURI.startsWith("/webjars")
                    || requestURI.startsWith("/upload")
                    || requestURI.startsWith("/image")
                    ) {
                req.getRequestDispatcher(requestURI).forward(req, resp);
            } else {
                User user = (User) httpRequest.getSession().getAttribute("user");
                if (user == null && !availableUrls.contains(requestURI)) {
                    ((HttpServletResponse) resp).sendRedirect("/login");
                } else {
                    req.getRequestDispatcher("/do" + requestURI).forward(req, resp);
                }
            }
            return;
        }

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {
        // TODO: 20.09.2016 load from properties
        availableUrls.add("/login");
        availableUrls.add("/");
        availableUrls.add("/register");
        availableUrls.add("/post");
        availableUrls.add("/set-locale");
    }

}
