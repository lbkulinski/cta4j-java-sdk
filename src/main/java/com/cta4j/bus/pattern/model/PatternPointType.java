package com.cta4j.bus.pattern.model;

import org.jspecify.annotations.NullMarked;

/**
 * Represents the type of point within a route or pattern geometry.
 */
@NullMarked
public enum PatternPointType {
    /**
     * Indicates a stop along the route.
     */
    STOP,

    /**
     * Indicates a waypoint along the route.
     */
    WAYPOINT
}
