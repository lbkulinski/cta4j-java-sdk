package com.cta4j.bus.model;

public enum TransitMode {
    NONE(0),

    BUS(1),

    FERRY(2),

    RAIL(3),

    PEOPLE_MOVER(4);

    private final int code;

    TransitMode(int code) {
        this.code = code;
    }

    public static TransitMode fromCode(int code) {
        for (TransitMode transitMode : TransitMode.values()) {
            if (transitMode.code == code) {
                return transitMode;
            }
        }

        throw new IllegalArgumentException("Invalid transit mode: " + code);
    }
}
