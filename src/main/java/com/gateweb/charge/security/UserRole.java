package com.gateweb.charge.security;

public enum UserRole {
    ROLE_USER(0L),
    ROLE_ADMIN(100L),
    ROLE_COMPANY_ADMIN(200L);

    public final Long roleNumber;

    UserRole(Long roleNumber) {
        this.roleNumber = roleNumber;
    }

    public static UserRole valueOfRoleNumber(Long roleNumber) {
        for (UserRole e : values()) {
            if (e.roleNumber.equals(roleNumber)) {
                return e;
            }
        }
        return null;
    }
}
