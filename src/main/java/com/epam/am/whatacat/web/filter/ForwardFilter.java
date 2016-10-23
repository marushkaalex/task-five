package com.epam.am.whatacat.web.filter;

import com.epam.am.whatacat.action.ActionFactory;
import com.epam.am.whatacat.model.Role;
import com.epam.am.whatacat.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOG = LoggerFactory.getLogger(ForwardFilter.class);

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
                LOG.debug("Static resource was requested");
                req.getRequestDispatcher(requestURI).forward(req, resp);
            } else if (ActionFactory.getInstance().getAction(httpRequest.getMethod() + requestURI) == null) {
                LOG.debug("Action was not found for {}/{}", httpRequest.getMethod(), requestURI);
                httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            } else {
                User user = (User) httpRequest.getSession().getAttribute("user");
                if (user == null && !isUrlAllowed(user, requestURI)) {
                    LOG.debug("User was redirected to log in");
                    ((HttpServletResponse) resp).sendRedirect("/login?fromUrl=" + URLEncoder.encode(requestURI, "UTF-8"));
                } else if (!isUrlAllowed(user, requestURI)) {
                    LOG.debug("Request uri '{}' is not allowed for user '{}'", requestURI, user);
                    httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
                } else {
                    LOG.debug("Request was forwarded to /do{}", requestURI);
                    req.getRequestDispatcher("/do" + requestURI).forward(req, resp);
                }
            }
            return;
        }

        LOG.debug("Request is not an instance of HttpServletRequest");
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {
        availabilityMap.put("/", RightContainer.any());
        availabilityMap.put("/login", RightContainer.any());
        availabilityMap.put("/logout", RightContainer.any());
        availabilityMap.put("/register", RightContainer.any());
        availabilityMap.put("/post", RightContainer.any());
        availabilityMap.put("/set-locale", RightContainer.any());
        availabilityMap.put("/user", RightContainer.any());
        availabilityMap.put("/admin/users", RightContainer.forRoles(ADMIN));
        availabilityMap.put("/admin/posts", RightContainer.forRoles(ADMIN));
        availabilityMap.put("/admin/edit-user", RightContainer.forRoles(ADMIN));
        availabilityMap.put("/moderator", RightContainer.forRoles(ADMIN, MODERATOR));
        availabilityMap.put("/create-post", RightContainer.authorized());
        availabilityMap.put("/profile", RightContainer.authorized());
        availabilityMap.put("/profile/save", RightContainer.authorized());
        availabilityMap.put("/rate-post", RightContainer.authorized());
        availabilityMap.put("/send-comment", RightContainer.authorized());
        availabilityMap.put("/moderator/moderate", RightContainer.forRoles(ADMIN, MODERATOR));
        availabilityMap.put("/admin/delete-user", RightContainer.forRoles(ADMIN));
        availabilityMap.put("/moderator/delete-comment", RightContainer.forRoles(ADMIN, MODERATOR));
        availabilityMap.put("/admin/edit-post", RightContainer.forRoles(ADMIN));
        availabilityMap.put("/admin/delete-post", RightContainer.forRoles(ADMIN));
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

        public static RightContainer forRoles(Role... roles) {
            return new RightContainer(roles);
        }

        public static RightContainer authorized() {
            return new RightContainer(Role.values());
        }
    }
}
