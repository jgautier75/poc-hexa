package com.acme.jga.domain.model.user;

public enum UserStatus {
    DRAFT(0), ACTIVE(1), INACTIVE(2);
    private final int value;

    private UserStatus(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public static UserStatus fromValue(int value) {
        for (UserStatus status : UserStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }
}
