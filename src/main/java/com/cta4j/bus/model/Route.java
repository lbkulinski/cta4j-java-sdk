package com.cta4j.bus.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings("ConstantConditions")
public record Route(
    String id,

    String name,

    String color,

    String designator,

    @Nullable
    String dataFeed
) {
    public Route {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }

        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }

        if (designator == null) {
            throw new IllegalArgumentException("designator must not be null");
        }

        if (color == null) {
            throw new IllegalArgumentException("color must not be null");
        }
    }
}
