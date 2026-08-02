package com.cta4j.alert.routestatus.internal.wire;

import com.cta4j.alert.common.internal.wire.CtaCdata;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaRouteInfo(
    @JsonProperty("Route") String route,
    @JsonProperty("RouteColorCode") String routeColorCode,
    @JsonProperty("RouteTextColor") String routeTextColor,
    @JsonProperty("ServiceId") String serviceId,
    @JsonProperty("RouteURL") CtaCdata routeUrl,
    @JsonProperty("RouteStatus") String routeStatus,
    @JsonProperty("RouteStatusColor") String routeStatusColor
) {
    public CtaRouteInfo {
        Objects.requireNonNull(route);
        Objects.requireNonNull(routeColorCode);
        Objects.requireNonNull(routeTextColor);
        Objects.requireNonNull(serviceId);
        Objects.requireNonNull(routeUrl);
        Objects.requireNonNull(routeStatus);
        Objects.requireNonNull(routeStatusColor);
    }
}
