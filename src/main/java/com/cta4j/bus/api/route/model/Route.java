package com.cta4j.bus.api.route.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record Route(
    String id,

    String name,

    String color,

    String designator,

    @Nullable
    String dataFeed
) {
    public Route(
        @Nullable String id,
        @Nullable String name,
        @Nullable String color,
        @Nullable String designator,
        @Nullable String dataFeed
    ) {
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

        this.id = id;
        this.name = name;
        this.color = color;
        this.designator = designator;
        this.dataFeed = dataFeed;
    }
}
