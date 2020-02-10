package com.microservices.lifecycle.bankstatement.controller.validator;

import com.microservices.lifecycle.bankstatement.model.StatementRecord;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ExtractValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return StatementRecord.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "status", "validation.message.field.required");
    }

}
