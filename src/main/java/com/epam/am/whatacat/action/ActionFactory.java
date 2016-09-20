package com.epam.am.whatacat.action;

import com.epam.am.whatacat.action.get.IndexAction;
import com.epam.am.whatacat.action.get.LogoutAction;
import com.epam.am.whatacat.action.get.ShowPageAction;
import com.epam.am.whatacat.action.get.ShowPostAction;
import com.epam.am.whatacat.action.post.CreatePostAction;
import com.epam.am.whatacat.action.post.LoginAction;
import com.epam.am.whatacat.action.post.RatePostAction;
import com.epam.am.whatacat.action.post.RegisterAction;

import java.util.HashMap;
import java.util.Map;

public class ActionFactory {
    private Map<String, Action> actionMap;

    public ActionFactory() {
        actionMap = new HashMap<>();
        actionMap.put("GET/", new IndexAction());
        actionMap.put("GET/login", new ShowPageAction("login"));
        actionMap.put("POST/login", new LoginAction());
        actionMap.put("GET/register", new ShowPageAction("register"));
        actionMap.put("POST/register", new RegisterAction());
        actionMap.put("GET/logout", new LogoutAction());
        actionMap.put("GET/create-post", new ShowPageAction("create-post"));
        actionMap.put("POST/create-post", new CreatePostAction());
        actionMap.put("POST/rate-post", new RatePostAction());
        actionMap.put("GET/post", new ShowPostAction());
        actionMap.put("GET/profile", new ShowPageAction("profile"));
    }

    public Action getAction(String actionName) {
        return actionMap.get(actionName);
    }
}
