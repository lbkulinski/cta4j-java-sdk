package com.cta4j.train.external.arrival;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaArrivalsResponse(
    CtaArrivalsCtatt ctatt
) {
}
