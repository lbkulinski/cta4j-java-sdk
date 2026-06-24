package com.cta4j.bus.prediction.model;

import org.jspecify.annotations.NullMarked;

/**
 * Represents the passenger load information for a prediction.
 */
@NullMarked
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
