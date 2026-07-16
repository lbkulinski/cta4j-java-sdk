package com.cta4j.alert.routestatus;

import com.cta4j.alert.routestatus.exception.Cta4jRouteStatusException;
import com.cta4j.alert.routestatus.model.RouteStatus;
import com.cta4j.alert.routestatus.model.ServiceType;
import com.cta4j.alert.routestatus.model.TrainRouteStatus;
import com.cta4j.common.train.TrainLine;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Provides access to route status-related endpoints of the CTA Alerts API.
 * <p>
 * This API allows retrieval of the status of all bus and train routes, or filtered by service type, bus route ID,
 * train line, or station ID.
 */
@NullMarked
public interface RouteStatusApi {
    /**
     * Retrieves the status of all bus and train routes.
     *
     * @return a {@link List} of {@link RouteStatus}es, or an empty {@link List} if no route statuses are found
     * @throws Cta4jRouteStatusException if the API returns an error response or the response cannot be parsed
     */
    List<RouteStatus> list();

    /**
     * Retrieves route statuses by their service types.
     *
     * @param types a {@link Collection} of service types
     * @return a {@link List} of {@link RouteStatus}es corresponding to the provided types, or an empty {@link List}
     * if no route statuses are found
     * @throws NullPointerException if {@code types} is {@code null} or contains {@code null} elements
     * @throws Cta4jRouteStatusException if the API returns an error response or the response cannot be parsed
     */
    List<RouteStatus> findByTypes(Collection<ServiceType> types);

    /**
     * Retrieves route statuses by a service type.
     *
     * @param type the service type
     * @return a {@link List} of {@link RouteStatus}es corresponding to the provided type, or an empty {@link List}
     * if no route statuses are found
     * @throws NullPointerException if {@code type} is {@code null}
     * @throws Cta4jRouteStatusException if the API returns an error response or the response cannot be parsed
     */
    default List<RouteStatus> findByType(ServiceType type) {
        Objects.requireNonNull(type);

        List<ServiceType> types = List.of(type);

        return this.findByTypes(types);
    }

    /**
     * Retrieves route statuses for the specified bus route IDs.
     *
     * @param routeIds a {@link Collection} of bus route IDs
     * @return a {@link List} of {@link RouteStatus}es associated with the bus route IDs, or an empty {@link List} if
     * no route statuses are found for the bus route IDs
     * @throws NullPointerException if {@code routeIds} is {@code null} or contains {@code null} elements
     * @throws Cta4jRouteStatusException if the API returns an error response or the response cannot be parsed
     */
    List<RouteStatus> findByBusRouteIds(Collection<String> routeIds);

    /**
     * Retrieves route statuses for the specified bus route ID.
     *
     * @param routeId the bus route ID
     * @return a {@link List} of {@link RouteStatus}es associated with the bus route ID, or an empty {@link List} if
     * no route statuses are found for the bus route ID
     * @throws NullPointerException if {@code routeId} is {@code null}
     * @throws Cta4jRouteStatusException if the API returns an error response or the response cannot be parsed
     */
    default List<RouteStatus> findByBusRouteId(String routeId) {
        Objects.requireNonNull(routeId);

        List<String> routeIds = List.of(routeId);

        return this.findByBusRouteIds(routeIds);
    }

    /**
     * Retrieves route statuses for the specified train lines.
     *
     * @param lines a {@link Collection} of train lines
     * @return a {@link List} of {@link TrainRouteStatus}es associated with the train lines, or an empty {@link List}
     * if no route statuses are found for the train lines
     * @throws NullPointerException if {@code lines} is {@code null} or contains {@code null} elements
     * @throws Cta4jRouteStatusException if the API returns an error response or the response cannot be parsed
     */
    List<TrainRouteStatus> findByLines(Collection<TrainLine> lines);

    /**
     * Retrieves route statuses for the specified train line.
     *
     * @param line the train line
     * @return a {@link List} of {@link TrainRouteStatus}es associated with the train line, or an empty {@link List}
     * if no route statuses are found for the train line
     * @throws NullPointerException if {@code line} is {@code null}
     * @throws Cta4jRouteStatusException if the API returns an error response or the response cannot be parsed
     */
    default List<TrainRouteStatus> findByLine(TrainLine line) {
        Objects.requireNonNull(line);

        List<TrainLine> lines = List.of(line);

        return this.findByLines(lines);
    }

    /**
     * Retrieves route statuses for the specified station ID.
     *
     * @param stationId the station ID
     * @return a {@link List} of {@link RouteStatus}es associated with the station ID, or an empty {@link List} if
     * no route statuses are found for the station ID
     * @throws NullPointerException if {@code stationId} is {@code null}
     * @throws Cta4jRouteStatusException if the API returns an error response or the response cannot be parsed
     */
    List<RouteStatus> findByStationId(String stationId);
}
