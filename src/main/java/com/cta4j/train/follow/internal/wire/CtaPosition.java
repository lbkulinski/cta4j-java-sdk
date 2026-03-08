package com.cta4j.train.follow.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaPosition(
    @Nullable
    Double lat,

    @Nullable
    Double lon,

    @Nullable
    Integer heading
) {
}
