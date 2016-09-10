package com.epam.am.whatacat.servlet;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionFactory;
import com.epam.am.whatacat.action.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "FrontControllerServlet", urlPatterns = "/do/*")
public class FrontControllerServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(FrontControllerServlet.class);
    private ActionFactory actionFactory;

    @Override
    public void init() throws ServletException {
        actionFactory = new ActionFactory();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String actionName = req.getMethod() + req.getPathInfo();
        log.info("Action name: {}", actionName);

        Action action = actionFactory.getAction(actionName);
        if (action == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            ActionResult actionResult = action.execute(req, resp);
            processResult(req, resp, actionResult);
        } catch (ActionException e) {
            throw new ServletException("Exception while executing action", e);
        }
    }

    private void processResult(HttpServletRequest req, HttpServletResponse resp, ActionResult result) throws IOException, ServletException {
        if (result.isRedirect()) {
            String path = req.getContextPath() + result.getView();
            resp.sendRedirect(path);
        } else {
            String path = "/WEB-INF/jsp/" + result.getView() + ".jsp";
            req.getRequestDispatcher(path).forward(req, resp);
        }
    }
}
