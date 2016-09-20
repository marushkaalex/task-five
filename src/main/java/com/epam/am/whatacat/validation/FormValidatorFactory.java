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
                        // TODO: 03.09.2016 use proper validator
                        .addFieldValidator("nickname", new NonEmptyFieldValidator("register.error.nickname"))
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
                        .addFieldValidator("old", new NonEmptyFieldValidator("profile.error.old-password.empty"))
                        .addFieldValidator("new", new NonEmptyFieldValidator("profile.error.new-password.empty"))
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
