package com.acme.jga.domain.otel;

import com.acme.jga.domain.exceptions.FunctionalException;
import io.opentelemetry.api.trace.Span;

@FunctionalInterface
public interface SpanOperation<T> {
    T execute(Span s) throws FunctionalException;
}
