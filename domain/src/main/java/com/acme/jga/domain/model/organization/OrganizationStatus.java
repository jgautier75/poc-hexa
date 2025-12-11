package com.acme.jga.domain.model.organization;

public enum OrganizationStatus {
    DRAFT(0),
    ACTIVE(1),
    INACTIVE(2);
    private final int value;

    OrganizationStatus(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public static OrganizationStatus fromValue(int value) {
        for (OrganizationStatus status : OrganizationStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }
}
