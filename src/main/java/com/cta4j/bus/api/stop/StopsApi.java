package com.cta4j.bus.api.stop;

import com.cta4j.bus.api.stop.model.Stop;
import com.cta4j.exception.Cta4jException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@NullMarked
public interface StopsApi {
    List<Stop> findByRouteIdAndDirection(@Nullable String routeId, @Nullable String direction);

    List<Stop> findByIds(@Nullable Collection<@Nullable String> stopIds);

    default Optional<Stop> findById(@Nullable String stopId) {
        if (stopId == null) {
            throw new IllegalArgumentException("stopId must not be null");
        }

        List<String> ids = List.of(stopId);

        List<Stop> stops = this.findByIds(ids);

        if (stops.isEmpty()) {
            return Optional.empty();
        } if (stops.size() > 1) {
            String message = String.format("Multiple stops found for ID: %s", stopId);

            throw new Cta4jException(message);
        }

        Stop stop = stops.getFirst();

        return Optional.of(stop);
    }
}
