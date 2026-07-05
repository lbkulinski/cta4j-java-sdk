package com.cta4j.bus.common.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaResponse<T>(
    @JsonProperty("bustime-response") T bustimeResponse
) {
    public CtaResponse {
        Objects.requireNonNull(bustimeResponse);
    }
}
