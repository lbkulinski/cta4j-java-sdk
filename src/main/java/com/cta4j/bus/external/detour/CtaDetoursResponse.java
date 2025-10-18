package com.cta4j.bus.external.detour;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaDetoursResponse(
    @JsonAlias("bustime-response")
    CtaDetoursBustimeResponse bustimeResponse
) {
}
