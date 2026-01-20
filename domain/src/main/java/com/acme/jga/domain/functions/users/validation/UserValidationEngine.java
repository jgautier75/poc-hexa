package com.acme.jga.domain.functions.users.validation;

import com.acme.jga.domain.model.user.User;
import com.acme.jga.domain.validation.ValidationEngine;
import com.acme.jga.domain.validation.ValidationException;
import com.acme.jga.domain.validation.ValidationResult;
import com.acme.jga.domain.validation.ValidationUtils;

public class UserValidationEngine implements ValidationEngine<User> {
    @Override
    public void validate(User user) throws ValidationException {
        ValidationResult validationResult = new ValidationResult(true, null);
        if (ValidationUtils.validateNotNull(validationResult, "firstName", user.firstName())) {
            ValidationUtils.validateTextLength(validationResult, "firstName", user.firstName(), 1, 50);
        }
        if (ValidationUtils.validateNotNull(validationResult, "lastName", user.lastName())) {
            ValidationUtils.validateTextLength(validationResult, "lastName", user.lastName(), 1, 50);
        }
        if (ValidationUtils.validateNotNull(validationResult, "email", user.email())) {
            ValidationUtils.validateTextLength(validationResult, "email", user.email(), 1, 50);
            ValidationUtils.validateEmail(validationResult, "email", user.email());
        }

        ValidationUtils.validateNotNull(validationResult, "status", user.status());

        if (ValidationUtils.validateNotNull(validationResult, "login", user.login())) {
            ValidationUtils.validateTextLength(validationResult, "login", user.login(), 1, 50);
        }

        if (!validationResult.isSuccess()) {
            throw new ValidationException(validationResult.getErrors());
        }
    }
}
