package com.cta4j.bus.external.prediction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaPredictionsBustimeResponse(
    List<CtaPredictionsPrd> prd
) {
}
