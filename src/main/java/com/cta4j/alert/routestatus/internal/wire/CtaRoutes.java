package com.cta4j.alert.routestatus.internal.wire;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaRoutes(
    @JsonProperty("TimeStamp")
    String timestamp,

    @JsonProperty("ErrorCode")
    String errorCode,

    @JsonProperty("ErrorMessage")
    @Nullable
    String errorMessage,

    @JsonProperty("RouteInfo")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Nullable
    List<CtaRouteInfo> routeInfo
) {
    public CtaRoutes {
        Objects.requireNonNull(timestamp);
        Objects.requireNonNull(errorCode);

        if (routeInfo != null) {
            routeInfo = List.copyOf(routeInfo);
        }
    }
}
