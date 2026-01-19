package com.cta4j.bus.api.detour;

import com.cta4j.bus.api.detour.model.Detour;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public interface DetoursApi {
    List<Detour> list();

    List<Detour> findByRouteId(String routeId);

    List<Detour> findByRouteIdAndDirection(String routeId, String direction);
}
