package com.cta4j.bus.route.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@NullMarked
public record Route(
    String id,

    String name,

    String color,

    String designator,

    @Nullable
    String dataFeed
) {
    public Route {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
        Objects.requireNonNull(color);
        Objects.requireNonNull(designator);
    }
}
