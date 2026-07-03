package com.cta4j.bus.pattern;

import com.cta4j.bus.common.BusApiConstants;
import com.cta4j.bus.common.exception.Cta4jBusException;
import com.cta4j.bus.pattern.model.RoutePattern;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Provides access to route pattern-related endpoints of the CTA BusTime API.
 * <p>
 * This API allows retrieval of route patterns by their IDs or by associated route IDs.
 */
@NullMarked
public interface PatternsApi {
    /**
     * Retrieves route patterns by their pattern IDs.
     *
     * @param patternIds a {@link Collection} of route pattern IDs
     * @return a {@link List} of {@link RoutePattern}s corresponding to the provided IDs, or an empty {@link List} if
     * no patterns are found
     * @throws NullPointerException if {@code patternIds} is {@code null} or contains {@code null} elements
     * @throws IllegalArgumentException if more than 10 pattern IDs are provided
     * @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
     */
    List<RoutePattern> findByIds(Collection<String> patternIds);

    /**
     * Retrieves a route pattern by its pattern ID.
     *
     * @param patternId the route pattern ID
     * @return an {@link Optional} containing the {@link RoutePattern} if found, or an empty {@link Optional} if no
     * pattern is found for the given ID
     * @throws NullPointerException if {@code patternId} is {@code null}
     * @throws Cta4jBusException if multiple route patterns are found for the given ID, or if the API returns an error
     * response or the response cannot be parsed
     */
    default Optional<RoutePattern> findById(String patternId) {
        Objects.requireNonNull(patternId);

        List<String> ids = List.of(patternId);

        List<RoutePattern> patterns = this.findByIds(ids);

        if (patterns.isEmpty()) {
            return Optional.empty();
        }

        if (patterns.size() > 1) {
            String message = "Multiple route patterns found for ID: %s".formatted(patternId);

            throw new Cta4jBusException(message, BusApiConstants.PATTERNS_ENDPOINT);
        }

        RoutePattern pattern = patterns.getFirst();

        return Optional.of(pattern);
    }

    /**
     * Retrieves all route patterns for the specified route ID.
     *
     * @param routeId the route ID
     * @return a {@link List} of {@link RoutePattern}s associated with the route ID, or an empty {@link List} if no
     * patterns are found for the route ID
     * @throws NullPointerException if {@code routeId} is {@code null}
     * @throws Cta4jBusException if the API returns an error response or the response cannot be parsed
     */
    List<RoutePattern> findByRouteId(String routeId);
}
