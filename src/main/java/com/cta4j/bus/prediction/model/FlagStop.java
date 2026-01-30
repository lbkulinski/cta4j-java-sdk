package com.cta4j.bus.prediction.model;

/**
 * Represents the flag-stop information for a prediction.
 */
public enum FlagStop {
    /**
     * Indicates that no flag-stop information is available.
     */
    UNDEFINED(-1),

    /**
     * Indicates a normal stop.
     */
    NORMAL(0),

    /**
     * Indicates a stop where passengers are both picked up and discharged.
     */
    PICKUP_AND_DISCHARGE(1),

    /**
     * Indicates a stop where only discharging of passengers occurs.
     */
    ONLY_DISCHARGE(2);

    private final int code;

    FlagStop(int code) {
        this.code = code;
    }

    /**
     * Gets the code associated with this flag-stop.
     *
     * @return the flag-stop code
     */
    public int getCode() {
        return this.code;
    }
}
