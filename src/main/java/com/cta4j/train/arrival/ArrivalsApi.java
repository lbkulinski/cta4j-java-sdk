package com.cta4j.train.arrival;

import com.cta4j.train.arrival.exception.Cta4jArrivalsException;
import com.cta4j.train.arrival.query.MapArrivalsQuery;
import com.cta4j.train.arrival.query.StopArrivalsQuery;
import com.cta4j.train.common.model.Arrival;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * Provides access to arrival-related endpoints of the CTA Train Tracker API.
 * <p>
 * This API allows retrieval of arrivals by map ID or stop ID.
 */
@NullMarked
public interface ArrivalsApi {
    /**
     * Retrieves arrivals by map ID.
     *
     * @param query the query parameters for fetching arrivals by map ID
     * @return a {@link List} of {@link Arrival}s corresponding to the provided map ID, or an empty {@link List} if no
     * arrivals are found
     * @throws NullPointerException if {@code query} is {@code null}
     * @throws Cta4jArrivalsException if the API returns an error response or the response cannot be parsed
     */
    List<Arrival> findByMapId(MapArrivalsQuery query);

    /**
     * Retrieves arrivals by stop ID.
     *
     * @param query the query parameters for fetching arrivals by stop ID
     * @return a {@link List} of {@link Arrival}s corresponding to the provided stop ID, or an empty {@link List} if no
     * arrivals are found
     * @throws NullPointerException if {@code query} is {@code null}
     * @throws Cta4jArrivalsException if the API returns an error response or the response cannot be parsed
     */
    List<Arrival> findByStopId(StopArrivalsQuery query);

    /**
     * Retrieves arrivals by map ID.
     *
     * @param mapId the map ID
     * @return a {@link List} of {@link Arrival}s corresponding to the provided map ID, or an empty {@link List} if no
     * arrivals are found
     * @throws NullPointerException if {@code mapId} is {@code null}
     * @throws Cta4jArrivalsException if the API returns an error response or the response cannot be parsed
     */
    default List<Arrival> findByMapId(String mapId) {
        MapArrivalsQuery query = MapArrivalsQuery.builder(mapId)
                                                 .build();

        return this.findByMapId(query);
    }

    /**
     * Retrieves arrivals by stop ID.
     *
     * @param stopId the stop ID
     * @return a {@link List} of {@link Arrival}s corresponding to the provided stop ID, or an empty {@link List} if no
     * arrivals are found
     * @throws NullPointerException if {@code stopId} is {@code null}
     * @throws Cta4jArrivalsException if the API returns an error response or the response cannot be parsed
     */
    default List<Arrival> findByStopId(String stopId) {
        StopArrivalsQuery query = StopArrivalsQuery.builder(stopId)
                                                   .build();

        return this.findByStopId(query);
    }
}
