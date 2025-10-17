package com.cta4j.external.bus.prediction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaPredictionsBustimeResponse(
    List<CtaPredictionsPrd> prd
) {
}
