package com.acme.jga.domain.functions.organizations.validation;

import com.acme.jga.domain.model.organization.Organization;
import com.acme.jga.domain.validation.ValidationEngine;
import com.acme.jga.domain.validation.ValidationException;
import com.acme.jga.domain.validation.ValidationResult;
import com.acme.jga.domain.validation.ValidationUtils;

import java.util.Collections;

public class OrganizationUpdateValidationEngine implements ValidationEngine<Organization> {

    @Override
    public void validate(Organization org) throws ValidationException {
        ValidationResult validationResult = new ValidationResult(true, Collections.emptyList());
        ValidationUtils.validateNotNull(validationResult, "code", org.code());
        ValidationUtils.validateNotNull(validationResult, "label", org.label());
        ValidationUtils.validateNotNull(validationResult, "kind", org.kind());
        ValidationUtils.validateNotNull(validationResult, "status", org.status());
        if (ValidationUtils.validateNotNull(validationResult, "country", org.country())) {
            ValidationUtils.validateCountry(validationResult, "country", org.country());
        }
        if (!validationResult.isSuccess()) {
            throw new ValidationException(validationResult.getErrors());
        }
    }
}
