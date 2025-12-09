package com.acme.jga.domain.exceptions;

public class FunctionalException extends Exception {
    private final String code;
    private final String scope;

    public FunctionalException(String scope, String code, String message) {
        super(message);
        this.code = code;
        this.scope = scope;
    }

    public String getCode() {
        return code;
    }
}
