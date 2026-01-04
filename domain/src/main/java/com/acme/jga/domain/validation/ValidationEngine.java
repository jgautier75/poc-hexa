package com.acme.jga.domain.validation;

public interface ValidationEngine<T> {
    void validate(T object) throws ValidationException;
}
