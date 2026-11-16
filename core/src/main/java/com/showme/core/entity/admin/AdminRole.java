package com.showme.core.entity.admin;

public enum AdminRole {
    SUPER,
    ADMIN;

    public String authority() {
        return "ROLE_" + name();
    }
}
