package com.acme.jga.domain.model.tenant;

public enum TenantStatus {
    DRAFT(0), ACTIVE(1), INACTIVE(2);

    private Integer value;

    TenantStatus(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static TenantStatus fromValue(Integer value) {
        for (TenantStatus status : TenantStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return TenantStatus.DRAFT;
    }
}
