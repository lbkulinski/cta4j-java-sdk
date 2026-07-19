package com.cta4j.alert.common.model;

import org.jspecify.annotations.NullMarked;

/**
 * Represents a category of CTA service — a bus route, train route, train station, or systemwide grouping.
 */
@NullMarked
public enum ServiceType {
    /**
     * Indicates bus routes.
     */
    BUS,

    /**
     * Indicates rail (train) routes.
     */
    RAIL,

    /**
     * Indicates train stations.
     */
    STATION,

    /**
     * Indicates systemwide categories, such as all routes, all bus routes, or all train routes.
     */
    SYSTEMWIDE
}
