package com.cta4j.bus.api.vehicle.model;

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

    public int getCode() {
        return this.code;
    }
}
