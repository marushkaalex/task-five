package com.epam.am.whatacat.validation;

public interface FieldValidator {
    /**
     * Validated form field
     * @param value field value
     * @return error message key for invalid field value or null if value is valid
     */
    String validate(String value);
}
