package com.cta4j.bus.api.common.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaResponse<T>(
    @JsonProperty("bustime-response")
    CtaBustimeResponse<T> bustimeResponse
) {
    public CtaResponse {
        Objects.requireNonNull(bustimeResponse);
    }
}
