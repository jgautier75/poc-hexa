package com.acme.jga.domain.output.functions.tenants;

import com.acme.jga.domain.exceptions.FunctionalException;
import com.acme.jga.domain.model.generic.CompositeId;
import io.opentelemetry.api.trace.Span;

public interface TenantExistsOutput {
    boolean existsByCode(String code);

    boolean existsByExternalId(String externalId, Span parentSpan) throws FunctionalException;

    boolean existsById(CompositeId compositeId, Span parentSpan) throws FunctionalException;
}
