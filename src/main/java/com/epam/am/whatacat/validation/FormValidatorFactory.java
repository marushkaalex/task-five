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
                        // TODO: 03.09.2016 user proper validator
                        .addFieldValidator("nickname", new NonEmptyFieldValidator("register.error.nickname"))
        );
    }

    public FormValidator getValidator(String formName) {
        return validatorMap.get(formName);
    }

    public static void init() {
        instance = new FormValidatorFactory();
    }

    public static FormValidatorFactory getInstance() {
        return instance;
    }
}
