package com.acme.jga.domain.functions.events.builders.organizations;

public enum EventOrganizationHolder {
    ;
    private static final EventOrganizationBuilder INSTANCE = new EventOrganizationBuilder();

    public static EventOrganizationBuilder getInstance() {
        return INSTANCE;
    }
}
