package com.cta4j.external.bus.detour;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaDetoursBustimeResponse(
    List<CtaDetour> dtrs
) {
}
