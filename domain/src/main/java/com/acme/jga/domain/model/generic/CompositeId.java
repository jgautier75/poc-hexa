package com.acme.jga.domain.model.generic;

public record CompositeId(Long internalId, String externalId) implements ExternalId {

    @Override
    public String get() {
        return externalId();
    }

    public IdKind kind() {
        if (internalId != null && externalId != null) {
            return IdKind.BOTH;
        } else if (internalId != null && externalId == null) {
            return IdKind.INTERNAL;
        } else if (internalId == null && externalId != null) {
            return IdKind.EXTERNAL;
        } else {
            return null;
        }
    }

}
