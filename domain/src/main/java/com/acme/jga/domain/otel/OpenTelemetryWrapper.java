package com.acme.jga.domain.otel;

import com.acme.jga.domain.exceptions.FunctionalException;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.TracerProvider;
import io.opentelemetry.context.Context;

import java.util.Map;

public class OpenTelemetryWrapper {
    private final TracerProvider tracerProvider;

    protected OpenTelemetryWrapper(TracerProvider tracerProvider) {
        this.tracerProvider = tracerProvider;
    }

    protected <T> T executeWithSpan(String instrumentationName, String spanName, Map<String, String> attributes, Span parentSpan, SpanOperation<T> operation) throws FunctionalException {
        Tracer tracer = tracerProvider.get(instrumentationName);
        Span span;
        if (parentSpan != null) {
            span = tracer.spanBuilder(spanName).setParent(Context.current().with(parentSpan)).startSpan();
        } else {
            span = tracer.spanBuilder(spanName).startSpan();
        }
        attributes.forEach(span::setAttribute);
        try {
            return operation.execute(span);
        } catch (Exception e) {
            span.setStatus(StatusCode.ERROR);
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }

    protected String nvl(String text) {
        return text != null ? text : "";
    }

}
