package com.bov.mt.constant;

public enum Mark {
    ING("ing"),AFTER("after");
    private final String type;
    Mark(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
