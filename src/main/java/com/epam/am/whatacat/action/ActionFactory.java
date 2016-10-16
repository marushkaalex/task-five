package com.epam.am.whatacat.action;

import com.epam.am.whatacat.action.admin.*;
import com.epam.am.whatacat.action.auth.LoginAction;
import com.epam.am.whatacat.action.auth.RegisterAction;
import com.epam.am.whatacat.action.auth.ShowLoginAction;
import com.epam.am.whatacat.action.base.IndexAction;
import com.epam.am.whatacat.action.base.LogoutAction;
import com.epam.am.whatacat.action.comment.DeleteCommentAction;
import com.epam.am.whatacat.action.comment.SendCommentAction;
import com.epam.am.whatacat.action.base.ShowPageAction;
import com.epam.am.whatacat.action.post.ShowPostAction;
import com.epam.am.whatacat.action.moderator.ModeratePostAction;
import com.epam.am.whatacat.action.moderator.ShowPostsToModerateAction;
import com.epam.am.whatacat.action.post.*;
import com.epam.am.whatacat.action.profile.*;

import java.util.HashMap;
import java.util.Map;

public class ActionFactory {
    private static ActionFactory instance;

    private Map<String, Action> actionMap;

    private ActionFactory() {
        actionMap = new HashMap<>();
        actionMap.put("GET/", new IndexAction());
        actionMap.put("GET/login", new ShowLoginAction());
        actionMap.put("POST/login", new LoginAction());
        actionMap.put("GET/register", new ShowPageAction("register"));
        actionMap.put("POST/register", new RegisterAction());
        actionMap.put("GET/logout", new LogoutAction());
        actionMap.put("GET/create-post", new ShowPageAction("create-post"));
        actionMap.put("POST/create-post", new CreatePostAction());
        actionMap.put("POST/rate-post", new RatePostAction());
        actionMap.put("GET/post", new ShowPostAction());
        actionMap.put("GET/profile", new ShowProfileAction());
        actionMap.put("POST/profile", new ChangePasswordAction());
        actionMap.put("POST/set-locale", new SetLocaleAction());
        actionMap.put("GET/user", new ShowUserAction());
        actionMap.put("GET/admin/users", new UserListAction());
        actionMap.put("GET/admin/posts", new PostListAction());
        actionMap.put("POST/send-comment", new SendCommentAction());
        actionMap.put("GET/admin/edit-user", new ShowEditUserAction());
        actionMap.put("GET/moderator", new ShowPostsToModerateAction());
        actionMap.put("POST/moderator/moderate", new ModeratePostAction());
        actionMap.put("POST/profile/save", new SaveUserProfileAction());
        actionMap.put("POST/admin/edit-user", new EditUserAction());
        actionMap.put("POST/admin/delete-user", new DeleteUserAction());
        actionMap.put("POST/admin/edit-post", new EditPostAction());
        actionMap.put("POST/moderator/delete-comment", new DeleteCommentAction());
        actionMap.put("GET/admin/edit-post", new ShowEditPostAction());
    }

    public Action getAction(String actionName) {
        return actionMap.get(actionName);
    }

    public static void init() {
        instance = new ActionFactory();
    }

    public static ActionFactory getInstance() {
        return instance;
    }
}
