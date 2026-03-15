package com.cta4j.train.station.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@NullMarked
@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaLocation(
    String latitude,

    String longitude,

    @Nullable
    @JsonProperty("human_address")
    String humanAddress
) {
    public CtaLocation {
        Objects.requireNonNull(latitude);
        Objects.requireNonNull(longitude);
    }
}
