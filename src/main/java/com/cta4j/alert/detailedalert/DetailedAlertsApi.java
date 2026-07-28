package com.cta4j.alert.detailedalert;

import com.cta4j.alert.common.model.AlertTrainLine;
import com.cta4j.alert.detailedalert.exception.Cta4jDetailedAlertsException;
import com.cta4j.alert.detailedalert.model.Alert;
import com.cta4j.alert.detailedalert.query.AlertsQuery;
import com.cta4j.alert.detailedalert.query.BusRouteAlertsQuery;
import com.cta4j.alert.detailedalert.query.LineAlertsQuery;
import com.cta4j.alert.detailedalert.query.StationAlertsQuery;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Provides access to detailed alert-related endpoints of the CTA Alerts API.
 * <p>
 * This API allows retrieval of all alerts, or filtered by bus route ID, train line, or station ID.
 */
@NullMarked
public interface DetailedAlertsApi {
    /**
     * Retrieves alerts matching the given query parameters.
     *
     * @param query the query parameters for fetching alerts
     * @return a {@link List} of {@link Alert}s matching the query, or an empty {@link List} if no alerts are found
     * @throws NullPointerException if {@code query} is {@code null}
     * @throws Cta4jDetailedAlertsException if the API returns an error response or the response cannot be parsed
     */
    List<Alert> list(AlertsQuery query);

    /**
     * Retrieves alerts using the default query parameters.
     *
     * @return a {@link List} of {@link Alert}s matching the default query, or an empty {@link List} if no alerts are
     * found
     * @throws Cta4jDetailedAlertsException if the API returns an error response or the response cannot be parsed
     */
    default List<Alert> list() {
        AlertsQuery query = AlertsQuery.builder()
                                       .build();

        return this.list(query);
    }

    /**
     * Retrieves alerts by bus route IDs.
     *
     * @param query the query parameters for fetching alerts by bus route IDs
     * @return a {@link List} of {@link Alert}s corresponding to the provided bus route IDs, or an empty {@link List}
     * if no alerts are found
     * @throws NullPointerException if {@code query} is {@code null}
     * @throws IllegalArgumentException if any of the query's route IDs matches a train line code (e.g., "Red"); use
     * {@link #findByLines(LineAlertsQuery)} instead
     * @throws Cta4jDetailedAlertsException if the API returns an error response or the response cannot be parsed
     */
    List<Alert> findByBusRouteIds(BusRouteAlertsQuery query);

    /**
     * Retrieves alerts by bus route IDs.
     *
     * @param routeIds a {@link Collection} of bus route IDs
     * @return a {@link List} of {@link Alert}s corresponding to the provided bus route IDs, or an empty {@link List}
     * if no alerts are found
     * @throws NullPointerException if {@code routeIds} is {@code null}, or if any element of {@code routeIds} is
     * {@code null}
     * @throws IllegalArgumentException if any of the {@code routeIds} matches a train line code (e.g., "Red"); use
     * {@link #findByLines(Collection)} instead
     * @throws Cta4jDetailedAlertsException if the API returns an error response or the response cannot be parsed
     */
    default List<Alert> findByBusRouteIds(Collection<String> routeIds) {
        Objects.requireNonNull(routeIds);

        List<String> routeIdsList = List.copyOf(routeIds);

        BusRouteAlertsQuery query = BusRouteAlertsQuery.builder(routeIdsList)
                                                       .build();

        return this.findByBusRouteIds(query);
    }

    /**
     * Retrieves alerts by bus route ID.
     *
     * @param routeId the bus route ID
     * @return a {@link List} of {@link Alert}s corresponding to the provided bus route ID, or an empty {@link List}
     * if no alerts are found
     * @throws NullPointerException if {@code routeId} is {@code null}
     * @throws IllegalArgumentException if {@code routeId} matches a train line code (e.g., "Red"); use
     * {@link #findByLine(AlertTrainLine)} instead
     * @throws Cta4jDetailedAlertsException if the API returns an error response or the response cannot be parsed
     */
    default List<Alert> findByBusRouteId(String routeId) {
        Objects.requireNonNull(routeId);

        List<String> routeIds = List.of(routeId);

        return this.findByBusRouteIds(routeIds);
    }

    /**
     * Retrieves alerts by train lines.
     *
     * @param query the query parameters for fetching alerts by train lines
     * @return a {@link List} of {@link Alert}s corresponding to the provided train lines, or an empty {@link List}
     * if no alerts are found
     * @throws NullPointerException if {@code query} is {@code null}
     * @throws Cta4jDetailedAlertsException if the API returns an error response or the response cannot be parsed
     */
    List<Alert> findByLines(LineAlertsQuery query);

    /**
     * Retrieves alerts by train lines.
     *
     * @param lines a {@link Collection} of train lines
     * @return a {@link List} of {@link Alert}s corresponding to the provided train lines, or an empty {@link List}
     * if no alerts are found
     * @throws NullPointerException if {@code lines} is {@code null}, or if any element of {@code lines} is
     * {@code null}
     * @throws Cta4jDetailedAlertsException if the API returns an error response or the response cannot be parsed
     */
    default List<Alert> findByLines(Collection<AlertTrainLine> lines) {
        Objects.requireNonNull(lines);

        List<AlertTrainLine> linesList = List.copyOf(lines);

        LineAlertsQuery query = LineAlertsQuery.builder(linesList)
                                               .build();

        return this.findByLines(query);
    }

    /**
     * Retrieves alerts by train line.
     *
     * @param line the train line
     * @return a {@link List} of {@link Alert}s corresponding to the provided train line, or an empty {@link List} if
     * no alerts are found
     * @throws NullPointerException if {@code line} is {@code null}
     * @throws Cta4jDetailedAlertsException if the API returns an error response or the response cannot be parsed
     */
    default List<Alert> findByLine(AlertTrainLine line) {
        Objects.requireNonNull(line);

        List<AlertTrainLine> lines = List.of(line);

        return this.findByLines(lines);
    }

    /**
     * Retrieves alerts by station IDs.
     *
     * @param query the query parameters for fetching alerts by station IDs
     * @return a {@link List} of {@link Alert}s corresponding to the provided station IDs, or an empty {@link List}
     * if no alerts are found
     * @throws NullPointerException if {@code query} is {@code null}
     * @throws Cta4jDetailedAlertsException if the API returns an error response or the response cannot be parsed
     */
    List<Alert> findByStationIds(StationAlertsQuery query);

    /**
     * Retrieves alerts by station IDs.
     *
     * @param stationIds a {@link Collection} of station IDs
     * @return a {@link List} of {@link Alert}s corresponding to the provided station IDs, or an empty {@link List}
     * if no alerts are found
     * @throws NullPointerException if {@code stationIds} is {@code null}, or if any element of {@code stationIds} is
     * {@code null}
     * @throws Cta4jDetailedAlertsException if the API returns an error response or the response cannot be parsed
     */
    default List<Alert> findByStationIds(Collection<String> stationIds) {
        Objects.requireNonNull(stationIds);

        List<String> stationIdsList = List.copyOf(stationIds);

        StationAlertsQuery query = StationAlertsQuery.builder(stationIdsList)
                                                     .build();

        return this.findByStationIds(query);
    }

    /**
     * Retrieves alerts by station ID.
     *
     * @param stationId the station ID
     * @return a {@link List} of {@link Alert}s corresponding to the provided station ID, or an empty {@link List} if
     * no alerts are found
     * @throws NullPointerException if {@code stationId} is {@code null}
     * @throws Cta4jDetailedAlertsException if the API returns an error response or the response cannot be parsed
     */
    default List<Alert> findByStationId(String stationId) {
        Objects.requireNonNull(stationId);

        List<String> stationIds = List.of(stationId);

        return this.findByStationIds(stationIds);
    }
}
