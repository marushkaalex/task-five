package com.epam.am.whatacat.validation;

import java.util.HashMap;
import java.util.Map;

public class FormValidatorFactory {
    private static FormValidatorFactory instance;

    private Map<String, FormValidator> validatorMap;

    private FormValidatorFactory() {
        validatorMap = new HashMap<>();

        validatorMap.put(
                "login",
                new FormValidator()
                        .addFieldValidator("email", new NonEmptyFieldValidator("login.error.email"))
        );

        validatorMap.put(
                "register",
                new FormValidator()
                        .addFieldValidator("email", new NonEmptyFieldValidator("register.error.email"))
                        .addFieldValidator("password", new LengthFieldValidator("register.error.password.length", 4, 30))
                        .addFieldValidator("nickname", new LengthFieldValidator("register.error.nickname.length", 4, 255))
        );

        validatorMap.put(
                "post",
                new FormValidator()
                        // TODO: 05.09.2016 use proper validator
                        .addFieldValidator("title", new NonEmptyFieldValidator("post.error.title.empty"))
                        .addFieldValidator("content", new NonEmptyFieldValidator("post.error.content.empty"))
        );

        validatorMap.put(
                "change-password",
                new FormValidator()
                        .addFieldValidator("oldPassword", new NonEmptyFieldValidator("profile.error.old-password.empty"))
                        .addFieldValidator("newPassword", new LengthFieldValidator("profile.error.new-password.length", 4, 30))
        );

        validatorMap.put(
                "send-comment",
                new FormValidator()
                        .addFieldValidator("text", new NonEmptyFieldValidator("comment.error.empty"))
                        .addFieldValidator("text", new LengthFieldValidator("comment.error.length", 1, 1000))
        );

        validatorMap.put(
                "save-user",
                new FormValidator()
                        .addFieldValidator("nickname", new LengthFieldValidator("profile.error.nickname.length", 4, 255))
                        .addFieldValidator("email", new NonEmptyFieldValidator("profile.error.email"))
        );

        validatorMap.put(
                "edit-user",
                new FormValidator()
                        .addFieldValidator("nickname", new LengthFieldValidator("profile.error.nickname.length", 4, 255))
                        .addFieldValidator("email", new NonEmptyFieldValidator("profile.error.email"))
        );
    }

    public static void init() {
        instance = new FormValidatorFactory();
    }

    public static FormValidatorFactory getInstance() {
        return instance;
    }

    public FormValidator getValidator(String formName) {
        return validatorMap.get(formName);
    }
}
