package com.cta4j.external.bus.route;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaRoutesBustimeResponse(
    List<CtaRoute> routes
) {
}
