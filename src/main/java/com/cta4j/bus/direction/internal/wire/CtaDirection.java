package com.cta4j.bus.direction.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaDirection(
    String id,

    String name
) {
    public CtaDirection {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
    }
}
