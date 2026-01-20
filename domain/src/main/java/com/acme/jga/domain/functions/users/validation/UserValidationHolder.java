package com.acme.jga.domain.functions.users.validation;

public enum UserValidationHolder {
    ;
    private static UserValidationEngine INSTANCE = new UserValidationEngine();

    public static UserValidationEngine getInstance() {
        return INSTANCE;
    }
}
