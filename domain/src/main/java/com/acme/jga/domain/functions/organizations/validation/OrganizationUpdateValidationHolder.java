package com.acme.jga.domain.functions.organizations.validation;

public enum OrganizationUpdateValidationHolder {
    ;
    private static final OrganizationUpdateValidationEngine INSTANCE = new OrganizationUpdateValidationEngine();

    public static OrganizationUpdateValidationEngine getInstance() {
        return INSTANCE;
    }
}
