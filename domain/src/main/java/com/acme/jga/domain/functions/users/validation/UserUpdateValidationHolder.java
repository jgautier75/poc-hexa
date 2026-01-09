package com.acme.jga.domain.functions.users.validation;

public enum UserUpdateValidationHolder {
    ;
    private static final UserUpdateValidationEngine INSTANCE = new UserUpdateValidationEngine();

    public static UserUpdateValidationEngine getInstance() {
        return INSTANCE;
    }
}
