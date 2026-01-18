package com.cta4j.bus.api.direction;

import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public interface DirectionsApi {
    List<String> findByRouteId(String routeId);
}
