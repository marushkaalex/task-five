package com.epam.am.whatacat.validation;

public abstract class BaseFieldValidator implements FieldValidator {
    protected String errorMessageKey;

    public BaseFieldValidator(String errorMessageKey) {
        this.errorMessageKey = errorMessageKey;
    }
}
