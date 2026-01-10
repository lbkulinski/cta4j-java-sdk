package com.cta4j.bus.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("ConstantConditions")
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaResponse<T>(
    @JsonProperty("bustime-response")
    CtaBustimeResponse<T> bustimeResponse
) {
    public CtaResponse {
        if (bustimeResponse == null) {
            throw new IllegalArgumentException("bustimeResponse must not be null");
        }
    }
}
