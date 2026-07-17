package com.cta4j.alert.routestatus.model;

import org.jspecify.annotations.NullMarked;

/**
 * Represents the type of service to filter route statuses by.
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
