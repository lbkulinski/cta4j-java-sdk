package com.cta4j.bus.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaResponse<T>(
    @JsonProperty("bustime-response")
    CtaBustimeResponse<T> bustimeResponse
) {
    public CtaResponse(@Nullable CtaBustimeResponse<T> bustimeResponse) {
        if (bustimeResponse == null) {
            throw new IllegalArgumentException("bustimeResponse must not be null");
        }

        this.bustimeResponse = bustimeResponse;
    }
}
