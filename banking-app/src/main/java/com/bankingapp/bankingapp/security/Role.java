package com.bankingapp.bankingapp.security;

public enum Role {
    ADMIN("ADMIN"), EMPLOYEE("EMPLOYEE"), USER("USER");

    public static final String ROLE_PREFIX = "ROLE_";

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public String getAuthority() {
        return ROLE_PREFIX + role;
    }
}
