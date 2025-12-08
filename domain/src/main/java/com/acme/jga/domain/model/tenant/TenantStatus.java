package com.acme.jga.domain.model;

public enum TenantStatus {
    DRAFT(0), ACTIVE(1), INACTIVE(2);

    private Integer value;

    TenantStatus(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
