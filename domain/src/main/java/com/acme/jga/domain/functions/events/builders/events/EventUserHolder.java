package com.acme.jga.domain.functions.events.builders.events;

public enum EventUserHolder {
    ;
    private static final EventUserBuilder INSTANCE = new EventUserBuilder();

    public static EventUserBuilder getInstance() {
        return INSTANCE;
    }
}
