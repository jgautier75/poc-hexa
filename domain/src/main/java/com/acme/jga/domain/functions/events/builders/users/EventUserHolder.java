package com.acme.jga.domain.functions.events.builders.users;

public enum EventUserHolder {
    ;
    private static final EventUserBuilder INSTANCE = new EventUserBuilder();

    public static EventUserBuilder getInstance() {
        return INSTANCE;
    }
}
