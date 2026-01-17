package com.cta4j.bus.external;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaBustimeResponse<T>(
    @Nullable
    List<CtaError> error,

    @Nullable
    @JsonAlias({
        "tm",
        "routes",
        "directions",
        "stops",
        "ptr",
        "prd",
        "dtrs",
        "vehicle",
    })
    T data
) {
}
