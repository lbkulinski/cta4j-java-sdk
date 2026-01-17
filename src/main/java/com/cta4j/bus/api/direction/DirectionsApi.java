package com.cta4j.bus.api.direction;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public interface DirectionsApi {
    List<String> findDirectionsByRouteId(@Nullable String routeId);
}
