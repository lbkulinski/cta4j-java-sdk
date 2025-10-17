package com.cta4j.external.bus.detour;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaDetoursBustimeResponse(
    List<CtaDetour> dtrs
) {
}
