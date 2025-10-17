package com.cta4j.external.train.arrival;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaArrivalsResponse(
    CtaArrivalsCtatt ctatt
) {
}
