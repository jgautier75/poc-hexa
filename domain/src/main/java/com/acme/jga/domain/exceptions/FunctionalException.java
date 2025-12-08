package com.acme.jga.domain.exceptions;

public class FunctionalException extends Exception {
    private final String code;

    public FunctionalException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
