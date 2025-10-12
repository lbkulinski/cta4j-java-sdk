package com.cta4j.model.bus;

/**
 * A bus route.
 *
 * @param id the unique identifier of the bus route
 * @param name the name of the bus route
 */
public record Route(
    String id,

    String name
) {
}
