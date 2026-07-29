package com.cta4j.alert.routestatus;

import com.cta4j.alert.common.model.AlertTrainLine;
import com.cta4j.alert.routestatus.exception.Cta4jRouteStatusException;
import com.cta4j.alert.routestatus.model.RouteStatus;
import com.cta4j.alert.common.model.ServiceType;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/// Provides access to route status-related endpoints of the CTA Alerts API.
///
/// This API allows retrieval of the status of all bus and train routes, or filtered by service type, bus route ID,
/// train line, or station ID.
@NullMarked
public interface RouteStatusApi {
    /// Retrieves the status of all bus and train routes.
    ///
    /// @return a [List] of [RouteStatus]es, or an empty [List] if no route statuses are found
    /// @throws Cta4jRouteStatusException if the API returns an error response or the response cannot be parsed
    List<RouteStatus> list();

    /// Retrieves route statuses by their service types.
    ///
    /// @param types a [Collection] of service types
    /// @return a [List] of [RouteStatus]es corresponding to the provided types, or an empty [List] if no route
    /// statuses are found
    /// @throws NullPointerException if `types` is `null` or contains `null` elements
    /// @throws Cta4jRouteStatusException if the API returns an error response or the response cannot be parsed
    List<RouteStatus> findByTypes(Collection<ServiceType> types);

    /// Retrieves route statuses by a service type.
    ///
    /// @param type the service type
    /// @return a [List] of [RouteStatus]es corresponding to the provided type, or an empty [List] if no route statuses
    /// are found
    /// @throws NullPointerException if `type` is `null`
    /// @throws Cta4jRouteStatusException if the API returns an error response or the response cannot be parsed
    default List<RouteStatus> findByType(ServiceType type) {
        Objects.requireNonNull(type);

        List<ServiceType> types = List.of(type);

        return this.findByTypes(types);
    }

    /// Retrieves route statuses for the specified bus route IDs.
    ///
    /// @param routeIds a [Collection] of bus route IDs
    /// @return a [List] of [RouteStatus]es associated with the bus route IDs, or an empty [List] if no route statuses
    /// are found for the bus route IDs
    /// @throws NullPointerException if `routeIds` is `null` or contains `null` elements
    /// @throws IllegalArgumentException if any of the `routeIds` matches a train line code (e.g., "Red");
    /// use [#findByLines(Collection)] instead
    /// @throws Cta4jRouteStatusException if the API returns an error response or the response cannot be parsed
    List<RouteStatus> findByBusRouteIds(Collection<String> routeIds);

    /// Retrieves route statuses for the specified bus route ID.
    ///
    /// @param routeId the bus route ID
    /// @return a [List] of [RouteStatus]es associated with the bus route ID, or an empty [List] if no route statuses
    /// are found for the bus route ID
    /// @throws NullPointerException if `routeId` is `null`
    /// @throws IllegalArgumentException if `routeId` matches a train line code (e.g., "Red");
    /// use [#findByLine(AlertTrainLine)] instead
    /// @throws Cta4jRouteStatusException if the API returns an error response or the response cannot be parsed
    default List<RouteStatus> findByBusRouteId(String routeId) {
        Objects.requireNonNull(routeId);

        List<String> routeIds = List.of(routeId);

        return this.findByBusRouteIds(routeIds);
    }

    /// Retrieves route statuses for the specified train lines.
    ///
    /// @param lines a [Collection] of train lines
    /// @return a [List] of [RouteStatus]es associated with the train lines, or an empty [List] if no route statuses
    /// are found for the train lines
    /// @throws NullPointerException if `lines` is `null` or contains `null` elements
    /// @throws Cta4jRouteStatusException if the API returns an error response or the response cannot be parsed
    List<RouteStatus> findByLines(Collection<AlertTrainLine> lines);

    /// Retrieves route statuses for the specified train line.
    ///
    /// @param line the train line
    /// @return a [List] of [RouteStatus]es associated with the train line, or an empty [List] if no route statuses are
    /// found for the train line
    /// @throws NullPointerException if `line` is `null`
    /// @throws Cta4jRouteStatusException if the API returns an error response or the response cannot be parsed
    default List<RouteStatus> findByLine(AlertTrainLine line) {
        Objects.requireNonNull(line);

        List<AlertTrainLine> lines = List.of(line);

        return this.findByLines(lines);
    }

    /// Retrieves route statuses for the specified station ID.
    ///
    /// @param stationId the station ID
    /// @return a [List] of [RouteStatus]es associated with the station ID, or an empty [List] if no route statuses are
    /// found for the station ID
    /// @throws NullPointerException if `stationId` is `null`
    /// @throws Cta4jRouteStatusException if the API returns an error response or the response cannot be parsed
    List<RouteStatus> findByStationId(String stationId);
}
