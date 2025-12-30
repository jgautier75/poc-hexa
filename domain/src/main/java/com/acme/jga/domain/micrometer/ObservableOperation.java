package com.acme.jga.domain.micrometer;

@FunctionalInterface
public interface ObservableOperation<T> {
    T execute();
}
