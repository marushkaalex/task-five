package com.epam.am.whatacat.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class FormValidator {

    private static final Logger LOG = LoggerFactory.getLogger(FormValidator.class);

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
            if (validator == null) {
                LOG.debug("There is no validator for field '{}'", fieldName);
                continue; // we don't need to validate this field
            }
            String errorMessageKey = validator.validate(fieldValue);
            if (errorMessageKey != null) {
                res.put(fieldName, errorMessageKey);
            }
        }

        return res;
    }
}
