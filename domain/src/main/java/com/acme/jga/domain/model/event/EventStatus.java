package com.acme.jga.domain.model.event;

public enum EventStatus {
    PENDING,
    PROCESSED,
    FAILED;

    public static EventStatus fromValue(String aValue) {
        if (PENDING.name().equals(aValue)) {
            return PENDING;
        } else if (PROCESSED.name().equals(aValue)) {
            return PROCESSED;
        } else if (FAILED.name().equals(aValue)) {
            return FAILED;
        } else {
            throw new RuntimeException("Unknown event status value [" + aValue + "]");
        }
    }

}
