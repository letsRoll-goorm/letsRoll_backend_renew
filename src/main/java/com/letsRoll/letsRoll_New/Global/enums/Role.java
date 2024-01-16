package com.letsRoll.letsRoll_New.Global.enums;

import lombok.Getter;

@Getter
public enum Role {
    GUEST("ROLE_GUEST"), USER("ROLE_USER");

    private final String role;

    Role(String role) {
        this.role = role;
    }
}
