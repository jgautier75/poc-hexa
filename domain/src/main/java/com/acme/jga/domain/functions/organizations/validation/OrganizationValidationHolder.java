package com.acme.jga.domain.functions.organizations.validation;

public enum OrganizationValidationHolder {
    ;
    private static final OrganizationValidationEngine INSTANCE = new OrganizationValidationEngine();

    public static OrganizationValidationEngine getInstance() {
        return INSTANCE;
    }
}
