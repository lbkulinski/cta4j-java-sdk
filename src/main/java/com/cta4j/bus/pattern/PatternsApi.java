package com.cta4j.bus.pattern;

import com.cta4j.bus.pattern.model.RoutePattern;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@NullMarked
public interface PatternsApi {
    List<RoutePattern> findByIds(Collection<String> patternIds);

    default List<RoutePattern> findById(String patternId) {
        Objects.requireNonNull(patternId);

        List<String> ids = List.of(patternId);

        return this.findByIds(ids);
    }

    List<RoutePattern> findByRouteId(String routeId);
}
