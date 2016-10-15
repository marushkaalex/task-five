package com.epam.am.whatacat.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormValidator {
    private Map<String, FieldValidator> fieldValidatorsMap;

    public FormValidator(Map<String, FieldValidator> fieldValidatorsMap) {
        this.fieldValidatorsMap = fieldValidatorsMap;
    }

    public FormValidator() {
        this(new HashMap<>());
    }

    public FormValidator addFieldValidator(String fieldName, FieldValidator validator) {
        fieldValidatorsMap.put(fieldName, validator);
        return this;
    }

    public Map<String, String> validate(Map<String, String[]> form) {
        Map<String, String> res = new HashMap<>();
        for (Map.Entry<String, String[]> entry : form.entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue()[0];
            FieldValidator validator = fieldValidatorsMap.get(fieldName);
            if (validator == null) continue; // we don't need to validate this field
            String errorMessageKey = validator.validate(fieldValue);
            if (errorMessageKey != null) {
                res.put(fieldName, errorMessageKey);
            }
        }

        return res;
    }
}
