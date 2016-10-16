package com.epam.am.whatacat.web.servlet;

import com.epam.am.whatacat.action.ActionFactory;
import com.epam.am.whatacat.model.Role;
import com.epam.am.whatacat.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

import static com.epam.am.whatacat.model.Role.*;

@WebFilter(filterName = "ForwardFilter")
public class ForwardFilter implements Filter {
    private Map<String, RightContainer> availabilityMap = new HashMap<>();

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if (req instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) req;
            HttpServletResponse httpResponse = (HttpServletResponse) resp;
            String requestURI = httpRequest.getRequestURI();

            if (requestURI.startsWith("/static")
                    || requestURI.startsWith("/webjars")
                    || requestURI.startsWith("/upload")
                    || requestURI.startsWith("/image")
                    ) {
                req.getRequestDispatcher(requestURI).forward(req, resp);
            } else if (ActionFactory.getInstance().getAction(httpRequest.getMethod() + requestURI) == null) {
                httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            } else {
                User user = (User) httpRequest.getSession().getAttribute("user");
                if (user == null && !isUrlAllowed(user, requestURI)) {
                    ((HttpServletResponse) resp).sendRedirect("/login?fromUrl=" + URLEncoder.encode(requestURI, "UTF-8"));
                } else if (!isUrlAllowed(user, requestURI)) {
                    httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
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
        availabilityMap.put("/", RightContainer.any());
        availabilityMap.put("/login", RightContainer.any());
        availabilityMap.put("/logout", RightContainer.any());
        availabilityMap.put("/register", RightContainer.any());
        availabilityMap.put("/post", RightContainer.any());
        availabilityMap.put("/set-locale", RightContainer.any());
        availabilityMap.put("/user", RightContainer.any());
        availabilityMap.put("/admin/users", RightContainer.of(ADMIN));
        availabilityMap.put("/admin/posts", RightContainer.of(ADMIN));
        availabilityMap.put("/admin/edit-user", RightContainer.of(ADMIN));
        availabilityMap.put("/moderator", RightContainer.of(ADMIN, MODERATOR));
        availabilityMap.put("/create-post", RightContainer.authorized());
        availabilityMap.put("/profile", RightContainer.authorized());
        availabilityMap.put("/profile/save", RightContainer.authorized());
        availabilityMap.put("/rate-post", RightContainer.authorized());
        availabilityMap.put("/send-comment", RightContainer.authorized());
        availabilityMap.put("/moderator/moderate", RightContainer.of(ADMIN, MODERATOR));
        availabilityMap.put("/admin/delete-user", RightContainer.of(ADMIN));
        availabilityMap.put("/moderator/delete-comment", RightContainer.of(ADMIN, MODERATOR));
        availabilityMap.put("/admin/edit-post", RightContainer.of(ADMIN));
    }

    private boolean isUrlAllowed(User user, String url) {
        RightContainer rightContainer = availabilityMap.get(url);
        return rightContainer != null && rightContainer.isAllowed(user);
    }

    private static class RightContainer {
        public List<Role> roleList;
        public boolean allowUnauthorized;

        private RightContainer(Role... roles) {
            this.roleList = Arrays.asList(roles);
        }

        private RightContainer(boolean allowUnauthorized) {
            this.allowUnauthorized = allowUnauthorized;
        }

        public boolean isAllowed(User user) {
            return allowUnauthorized || (user != null && roleList.contains(user.getRole()));
        }

        public static RightContainer any() {
            return new RightContainer(true);
        }

        public static RightContainer of(Role... roles) {
            return new RightContainer(roles);
        }

        public static RightContainer authorized() {
            return new RightContainer(Role.values());
        }
    }
}
