package com.cta4j.train.arrival;

import com.cta4j.train.arrival.model.Arrival;
import com.cta4j.train.arrival.query.MapArrivalQuery;
import com.cta4j.train.arrival.query.StopArrivalQuery;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * Provides access to arrival-related endpoints of the CTA Train Tracker API.
 * <p>
 * This API allows retrieval of arrival information by map ID or stop ID.
 */
@NullMarked
public interface ArrivalsApi {
    /**
     * Retrieves arrival information by map ID.
     *
     * @param query the query parameters for fetching arrival information by map ID
     * @return a {@link List} of {@link Arrival}s corresponding to the provided map ID, or an empty {@link List} if no
     * arrivals are found
     * @throws NullPointerException if {@code query} is {@code null}
     */
    List<Arrival> findByMapId(MapArrivalQuery query);

    /**
     * Retrieves arrival information by stop ID.
     *
     * @param query the query parameters for fetching arrival information by stop ID
     * @return a {@link List} of {@link Arrival}s corresponding to the provided stop ID, or an empty {@link List} if no
     * arrivals are found
     * @throws NullPointerException if {@code query} is {@code null}
     */
    List<Arrival> findByStopId(StopArrivalQuery query);

    /**
     * Retrieves arrival information by map ID.
     *
     * @param mapId the map ID
     * @return a {@link List} of {@link Arrival}s corresponding to the provided map ID, or an empty {@link List} if no
     * arrivals are found
     * @throws NullPointerException if {@code mapId} is {@code null}
     */
    default List<Arrival> findByMapId(String mapId) {
        MapArrivalQuery query = MapArrivalQuery.builder(mapId)
                                               .build();

        return this.findByMapId(query);
    }

    /**
     * Retrieves arrival information by stop ID.
     *
     * @param stopId the stop ID
     * @return a {@link List} of {@link Arrival}s corresponding to the provided stop ID, or an empty {@link List} if no
     * arrivals are found
     * @throws NullPointerException if {@code stopId} is {@code null}
     */
    default List<Arrival> findByStopId(String stopId) {
        StopArrivalQuery query = StopArrivalQuery.builder(stopId)
                                                 .build();

        return this.findByStopId(query);
    }
}
