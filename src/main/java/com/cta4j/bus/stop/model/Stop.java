package com.cta4j.bus.stop.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@NullMarked
public record Stop(
    String id,

    String name,

    BigDecimal latitude,

    BigDecimal longitude,

    @Nullable
    List<Integer> detoursAdded,

    @Nullable
    List<Integer> detoursRemoved,

    @Nullable
    Integer gtfsSequence,

    @Nullable
    Boolean adaAccessible
) {
    public Stop {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
        Objects.requireNonNull(latitude);
        Objects.requireNonNull(longitude);

        if (detoursAdded != null) {
            detoursAdded.forEach(Objects::requireNonNull);

            detoursAdded = List.copyOf(detoursAdded);
        }

        if (detoursRemoved != null) {
            detoursRemoved.forEach(Objects::requireNonNull);

            detoursRemoved = List.copyOf(detoursRemoved);
        }
    }
}
