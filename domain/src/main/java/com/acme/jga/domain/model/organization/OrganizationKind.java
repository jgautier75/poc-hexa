package com.acme.jga.domain.model.organization;

public enum OrganizationKind {
    ROOT(0),
    BU(1),
    COMMUNITY(2);

    private Integer value;

    OrganizationKind(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static OrganizationKind fromValue(Integer value) {
        for (OrganizationKind kind : OrganizationKind.values()) {
            if (kind.value.equals(value)) {
                return kind;
            }
        }
        return null;
    }
}
