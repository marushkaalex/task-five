package com.epam.am.whatacat.validation;

public class NonEmptyFieldValidator extends BaseFieldValidator {
    public NonEmptyFieldValidator(String errorMessageKey) {
        super(errorMessageKey);
    }

    @Override
    public String validate(String value) {
        return (value == null || value.isEmpty()) ? errorMessageKey : null;
    }
}
