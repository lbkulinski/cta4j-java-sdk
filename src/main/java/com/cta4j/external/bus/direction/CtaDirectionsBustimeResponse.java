package com.cta4j.external.bus.direction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaDirectionsBustimeResponse(
    List<CtaDirection> directions
) {
}
