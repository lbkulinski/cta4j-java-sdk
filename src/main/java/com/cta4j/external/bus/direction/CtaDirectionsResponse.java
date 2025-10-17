package com.cta4j.external.bus.direction;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaDirectionsResponse(
    @JsonAlias("bustime-response")
    CtaDirectionsBustimeResponse bustimeResponse
) {
}
