package com.cta4j.bus.api.prediction.model;

public enum FlagStop {
    UNDEFINED(-1),

    NORMAL(0),

    PICKUP_AND_DISCHARGE(1),

    ONLY_DISCHARGE(2);

    private final int code;

    FlagStop(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
