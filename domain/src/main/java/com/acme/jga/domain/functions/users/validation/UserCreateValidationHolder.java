package com.acme.jga.domain.functions.users.validation;

public enum UserCreateValidationHolder {
    ;
    private static UserCreateValidationEngine INSTANCE = new UserCreateValidationEngine();

    public static UserCreateValidationEngine getInsance() {
        return INSTANCE;
    }
}
