package com.cta4j.bus.model;

/**
 * A bus route.
 *
 * @param id the unique identifier of the bus route
 * @param name the name of the bus route
 * @param description the description of the bus route
 * @param color the color associated with the bus route
 */
public record Route(
    String id,

    String name,

    String description,

    String color
) {
}
