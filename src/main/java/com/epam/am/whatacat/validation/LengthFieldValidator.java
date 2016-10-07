package com.epam.am.whatacat.validation;

public class LengthFieldValidator extends BaseFieldValidator {
    private int min;
    private int max;

    public LengthFieldValidator(String errorMessageKey, int min, int max) {
        super(errorMessageKey);
        this.min = min;
        this.max = max;
    }

    @Override
    public String validate(String value) {
        return (value.length() >= min && value.length() <= max) ? null : errorMessageKey;
    }
}
