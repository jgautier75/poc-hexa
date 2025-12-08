package com.acme.jga.domain.model.generic;

public record CompositeId(IdKind idKind, Long internalId, String externalId) {

}
