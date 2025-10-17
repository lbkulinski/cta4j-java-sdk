package com.cta4j.external.bus.prediction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaPredictionsBustimeResponse(
    List<CtaPredictionsPrd> prd
) {
}
