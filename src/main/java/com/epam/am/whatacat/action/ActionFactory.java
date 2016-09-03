package com.epam.am.whatacat.action;

import com.epam.am.whatacat.action.get.ShowPageAction;
import com.epam.am.whatacat.action.post.LoginAction;
import com.epam.am.whatacat.action.post.RegisterAction;

import java.util.HashMap;
import java.util.Map;

public class ActionFactory {
    private Map<String, Action> actionMap;

    public ActionFactory() {
        actionMap = new HashMap<>();
        actionMap.put("GET/login", new ShowPageAction("login"));
        actionMap.put("POST/login", new LoginAction());
        actionMap.put("GET/register", new ShowPageAction("register"));
        actionMap.put("POST/register", new RegisterAction());
    }

    public Action getAction(String actionName) {
        return actionMap.get(actionName);
    }
}
