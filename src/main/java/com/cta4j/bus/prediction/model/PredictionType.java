package com.cta4j.bus.prediction.model;

import org.jspecify.annotations.NullMarked;

/**
 * Represents the type of bus prediction.
 */
@NullMarked
public enum PredictionType {
    /**
     * Indicates an arrival prediction.
     */
    ARRIVAL,

    /**
     * Indicates a departure prediction.
     */
    DEPARTURE
}
