package com.acme.jga.domain.functions.organizations.validation;

public enum OrganizationCreateValidationHolder {
    ;
    private static final OrganizationCreateValidationEngine INSTANCE = new OrganizationCreateValidationEngine();

    public static OrganizationCreateValidationEngine getInstance() {
        return INSTANCE;
    }
}
