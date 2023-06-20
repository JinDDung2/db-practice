package com.example.fasns.enums;

import lombok.Getter;

@Getter
public enum MemberRole {
    ROLE_INFLUENCED(2, "ROLE_INFLUENCED", null),
    ROLE_BASIC(1, "ROLE_BASIC", ROLE_INFLUENCED),
    ROLE_ADMIN(0, "ROLE_ADMIN", null);

    private final int value;
    private final String name;
    private final MemberRole nextLevel;

    MemberRole(int value, String name, MemberRole nextLevel) {
        this.value = value;
        this.name = name;
        this.nextLevel = nextLevel;
    }

    public static MemberRole valueOf(int value) {
        switch (value) {
            case 0: return ROLE_ADMIN;
            case 1: return ROLE_BASIC;
            case 2: return ROLE_INFLUENCED;
            default: throw new AssertionError("Unknown value: " + value);
        }
    }
}
