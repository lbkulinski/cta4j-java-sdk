package com.cta4j.bus.stop;

import com.cta4j.bus.stop.model.Stop;
import com.cta4j.exception.Cta4jException;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@NullMarked
public interface StopsApi {
    List<Stop> findByRouteIdAndDirection(String routeId, String direction);

    List<Stop> findByIds(Collection<String> stopIds);

    default Optional<Stop> findById(String stopId) {
        Objects.requireNonNull(stopId);

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
