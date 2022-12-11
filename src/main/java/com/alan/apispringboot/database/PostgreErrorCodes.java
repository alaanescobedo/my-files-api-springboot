package com.alan.apispringboot.database;

public enum PostgreErrorCodes {
    UNIQUE_VIOLATION(23505);

    private final Integer code;

    PostgreErrorCodes(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
