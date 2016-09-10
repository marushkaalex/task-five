package com.epam.am.whatacat.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "ForwardFilter", urlPatterns = "/*")
public class ForwardFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if (req instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) req;
            String requestURI = httpRequest.getRequestURI();
            if (requestURI.startsWith("/static") || requestURI.startsWith("/webjars")) {
                chain.doFilter(req, resp);
            } else {
                req.getRequestDispatcher("/do" + requestURI).forward(req, resp);
            }
            return;
        }

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
