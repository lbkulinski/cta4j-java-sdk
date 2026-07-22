package com.cta4j.alert.routestatus.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaRouteStatusResponse(
    @JsonProperty("CTARoutes") CtaRoutes ctaRoutes
) {
    public CtaRouteStatusResponse {
        Objects.requireNonNull(ctaRoutes);
    }
}
