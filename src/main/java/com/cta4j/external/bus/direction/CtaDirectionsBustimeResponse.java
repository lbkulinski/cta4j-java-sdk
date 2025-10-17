package com.cta4j.external.bus.direction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaDirectionsBustimeResponse(
    List<CtaDirection> directions
) {
}
