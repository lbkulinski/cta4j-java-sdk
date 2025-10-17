package com.cta4j.external.bus.prediction;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaPredictionsResponse(
    @JsonAlias("bustime-response")
    CtaPredictionsBustimeResponse bustimeResponse
) {
}
