package com.cta4j.train.arrival;

import com.cta4j.train.arrival.exception.Cta4jArrivalsException;
import com.cta4j.train.arrival.query.MapArrivalsQuery;
import com.cta4j.train.arrival.query.StopArrivalsQuery;
import com.cta4j.train.common.model.Arrival;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/// Provides access to arrival-related endpoints of the CTA Train Tracker API.
///
/// This API allows retrieval of arrivals by map IDs or stop IDs.
@NullMarked
public interface ArrivalsApi {
    /// Retrieves arrivals by map IDs.
    ///
    /// @param query the query parameters for fetching arrivals by map IDs
    /// @return a [List] of [Arrival]s corresponding to the provided map IDs, or an empty [List] if no arrivals are
    /// found
    /// @throws NullPointerException if `query` is `null`
    /// @throws Cta4jArrivalsException if the API returns an error response or the response cannot be parsed
    List<Arrival> findByMapIds(MapArrivalsQuery query);

    /// Retrieves arrivals by map IDs.
    ///
    /// @param mapIds a [Collection] of map IDs
    /// @return a [List] of [Arrival]s corresponding to the provided map IDs, or an empty [List] if no arrivals are
    /// found
    /// @throws NullPointerException if `mapIds` is `null`, or if any element of `mapIds` is `null`
    /// @throws Cta4jArrivalsException if the API returns an error response or the response cannot be parsed
    default List<Arrival> findByMapIds(Collection<String> mapIds) {
        Objects.requireNonNull(mapIds);

        List<String> mapIdsList = List.copyOf(mapIds);

        MapArrivalsQuery query = MapArrivalsQuery.builder(mapIdsList)
                                                 .build();

        return this.findByMapIds(query);
    }

    /// Retrieves arrivals by map ID.
    ///
    /// @param mapId the map ID
    /// @return a [List] of [Arrival]s corresponding to the provided map ID, or an empty [List] if no arrivals are
    /// found
    /// @throws NullPointerException if `mapId` is `null`
    /// @throws Cta4jArrivalsException if the API returns an error response or the response cannot be parsed
    default List<Arrival> findByMapId(String mapId) {
        Objects.requireNonNull(mapId);

        List<String> mapIds = List.of(mapId);

        MapArrivalsQuery query = MapArrivalsQuery.builder(mapIds)
                                                 .build();

        return this.findByMapIds(query);
    }

    /// Retrieves arrivals by stop IDs.
    ///
    /// @param query the query parameters for fetching arrivals by stop IDs
    /// @return a [List] of [Arrival]s corresponding to the provided stop IDs, or an empty [List] if no arrivals are
    /// found
    /// @throws NullPointerException if `query` is `null`
    /// @throws Cta4jArrivalsException if the API returns an error response or the response cannot be parsed
    List<Arrival> findByStopIds(StopArrivalsQuery query);

    /// Retrieves arrivals by stop IDs.
    ///
    /// @param stopIds a [Collection] of stop IDs
    /// @return a [List] of [Arrival]s corresponding to the provided stop IDs, or an empty [List] if no arrivals are
    /// found
    /// @throws NullPointerException if `stopIds` is `null`, or if any element of `stopIds` is `null`
    /// @throws Cta4jArrivalsException if the API returns an error response or the response cannot be parsed
    default List<Arrival> findByStopIds(Collection<String> stopIds) {
        Objects.requireNonNull(stopIds);

        List<String> stopIdsList = List.copyOf(stopIds);

        StopArrivalsQuery query = StopArrivalsQuery.builder(stopIdsList)
                                                   .build();

        return this.findByStopIds(query);
    }

    /// Retrieves arrivals by stop ID.
    ///
    /// @param stopId the stop ID
    /// @return a [List] of [Arrival]s corresponding to the provided stop ID, or an empty [List] if no arrivals are
    /// found
    /// @throws NullPointerException if `stopId` is `null`
    /// @throws Cta4jArrivalsException if the API returns an error response or the response cannot be parsed
    default List<Arrival> findByStopId(String stopId) {
        Objects.requireNonNull(stopId);

        List<String> stopIds = List.of(stopId);

        StopArrivalsQuery query = StopArrivalsQuery.builder(stopIds)
                                                   .build();

        return this.findByStopIds(query);
    }
}
