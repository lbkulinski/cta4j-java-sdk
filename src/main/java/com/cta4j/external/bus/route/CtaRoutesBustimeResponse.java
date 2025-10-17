package com.cta4j.external.bus.route;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaRoutesBustimeResponse(
    List<CtaRoute> routes
) {
}
