package com.acme.jga.domain.functions.users.validation;

import com.acme.jga.domain.model.user.User;
import com.acme.jga.domain.validation.ValidationEngine;
import com.acme.jga.domain.validation.ValidationException;
import com.acme.jga.domain.validation.ValidationResult;
import com.acme.jga.domain.validation.ValidationUtils;

public class UserUpdateValidationEngine implements ValidationEngine<User> {
    @Override
    public void validate(User user) throws ValidationException {
        ValidationResult validationResult = new ValidationResult(true, null);
        ValidationUtils.validateNotNull(validationResult, "firstName", user.firstName());
        ValidationUtils.validateNotNull(validationResult, "lastName", user.lastName());
        ValidationUtils.validateNotNull(validationResult, "email", user.email());
        ValidationUtils.validateNotNull(validationResult, "status", user.status());
        ValidationUtils.validateNotNull(validationResult, "login", user.login());
        if (!validationResult.isSuccess()) {
            throw new ValidationException(validationResult.getErrors());
        }
    }
}
