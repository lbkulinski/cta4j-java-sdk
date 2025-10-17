package com.cta4j.external.train.arrival;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaArrivalsResponse(
    CtaArrivalsCtatt ctatt
) {
}
