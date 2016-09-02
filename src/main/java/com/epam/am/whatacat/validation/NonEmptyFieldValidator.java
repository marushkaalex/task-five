package com.epam.am.whatacat.validation;

public class NonEmptyFieldValidator implements FieldValidator {
    private String errorMessageKey;

    public NonEmptyFieldValidator(String errorMessageKey) {
        this.errorMessageKey = errorMessageKey;
    }

    @Override
    public String validate(String value) {
        return (value == null || value.isEmpty()) ? errorMessageKey : null;
    }
}
