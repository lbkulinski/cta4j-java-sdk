package com.cta4j.bus.external;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jspecify.annotations.Nullable;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaBustimeResponse<T>(
    @Nullable
    List<CtaError> error,

    @Nullable
    @JsonAlias({
        "routes",
        "directions",
        "stops",
        "prd",
        "dtrs",
        "vehicle",
    })
    List<T> data
) {
}
