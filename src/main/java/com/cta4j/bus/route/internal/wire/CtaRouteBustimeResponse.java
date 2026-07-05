package com.cta4j.bus.route.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaRouteBustimeResponse(
    @Nullable List<CtaRouteError> error,
    @Nullable List<CtaRoute> routes
) {
    public CtaRouteBustimeResponse {
        if (error != null) {
            error = List.copyOf(error);
        }

        if (routes != null) {
            routes = List.copyOf(routes);
        }
    }
}
