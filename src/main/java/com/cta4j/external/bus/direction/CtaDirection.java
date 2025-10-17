package com.cta4j.external.bus.direction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaDirection(
    String id,

    String name
) {
}
