package com.cta4j.bus.prediction.model;

/**
 * Represents the passenger load information for a prediction.
 */
public enum PassengerLoad {
    /**
     * Indicates that the bus is full.
     */
    FULL,

    /**
     * Indicates that the bus is half full.
     */
    HALF_EMPTY,

    /**
     * Indicates that the bus is empty.
     */
    EMPTY,

    /**
     * Indicates that no passenger load information is available.
     */
    UNKNOWN
}
