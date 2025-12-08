package com.acme.jga.domain.exceptions;

public class TechnicalException extends RuntimeException {
    public TechnicalException(String message) {
        super(message);
    }

    public TechnicalException(String message, Exception e) {
        super(message, e);
    }
}
