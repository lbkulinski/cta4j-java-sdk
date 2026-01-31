package com.cta4j.bus.stop;

import com.cta4j.bus.stop.model.Stop;
import com.cta4j.exception.Cta4jException;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Provides access to stop-related endpoints of the CTA BusTime API.
 * <p>
 * This API allows retrieval of stops by route ID and direction, as well as by stop IDs.
 */
@NullMarked
public interface StopsApi {
    /**
     * Retrieves stops by route ID and direction.
     *
     * @param routeId the route ID
     * @param direction the direction (e.g., "Northbound", "Southbound")
     * @return a {@link List} of {@link Stop}s corresponding to the provided route ID and direction, or an empty
     * {@link List} if no stops are found
     * @throws NullPointerException if {@code routeId} or {@code direction} is {@code null}
     */
    List<Stop> findByRouteIdAndDirection(String routeId, String direction);

    /**
     * Retrieves stops by their IDs.
     *
     * @param stopIds a {@link Collection} of stop IDs
     * @return a {@link List} of {@link Stop}s corresponding to the provided stop IDs, or an empty {@link List} if no
     * stops are found
     * @throws NullPointerException if {@code stopIds} is {@code null} or contains {@code null} elements
     */
    List<Stop> findByIds(Collection<String> stopIds);

    /**
     * Retrieves a stop by its ID.
     *
     * @param stopId the stop ID
     * @return an {@link Optional} containing the {@link Stop} if found, or an empty {@link Optional} if no stop exists
     * with the given ID
     * @throws NullPointerException if {@code stopId} is {@code null}
     * @throws Cta4jException if multiple stops are found for the given ID
     */
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
