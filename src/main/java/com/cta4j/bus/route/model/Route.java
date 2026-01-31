package com.cta4j.bus.route.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a bus route.
 *
 * <p>
 *     <b>NOTE:</b> {@code dataFeed} is not well-documented by the CTA. As such, its presence here is primarily for
 *     completeness and may not be populated or described correctly.
 * </p>
 *
 * @param id the alphanumeric designator of this route (e.g., "22", "J14", "X9")
 * @param name the common name of this route (e.g., "Clark", "Jeffery Jump", "Ashland Express")
 * @param color the color of this route used in maps (e.g., "#ffffff")
 * @param designator the language-specific route designator of this route, intended for display
 * @param dataFeed the data feed identifier for this route, if applicable
 */
@NullMarked
public record Route(
    String id,

    String name,

    String color,

    String designator,

    @Nullable
    String dataFeed
) {
    /**
     * Constructs a {@code Route}.
     *
     * @param id the alphanumeric designator of the route (e.g., "22", "J14", "X9")
     * @param name the common name of the route (e.g., "Clark", "Jeffery Jump", "Ashland Express")
     * @param color the color of the route used in maps (e.g., "#ffffff")
     * @param designator the language-specific route designator of the route, intended for display
     * @param dataFeed the data feed identifier for the route, if applicable
     * @throws NullPointerException if {@code id}, {@code name}, {@code color}, or {@code designator} is {@code null}
     */
    public Route {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
        Objects.requireNonNull(color);
        Objects.requireNonNull(designator);
    }
}
