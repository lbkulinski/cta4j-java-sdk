package com.cta4j.bus.api.pattern;

import com.cta4j.bus.api.pattern.model.RoutePattern;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;

@NullMarked
public interface PatternsApi {
    List<RoutePattern> findByIds(@Nullable Collection<@Nullable String> patternIds);

    default List<RoutePattern> findById(@Nullable String patternId) {
        if (patternId == null) {
            throw new IllegalArgumentException("patternId must not be null");
        }

        List<String> ids = List.of(patternId);

        return this.findByIds(ids);
    }

    List<RoutePattern> findByRouteId(@Nullable String routeId);
}
