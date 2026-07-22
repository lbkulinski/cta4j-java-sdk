package com.cta4j.alert.detailedalert;

import com.cta4j.alert.detailedalert.model.Alert;
import com.cta4j.alert.detailedalert.query.AlertsQuery;
import com.cta4j.alert.detailedalert.query.BusRouteAlertsQuery;
import com.cta4j.alert.detailedalert.query.LineAlertsQuery;
import com.cta4j.alert.detailedalert.query.StationAlertsQuery;
import com.cta4j.common.train.TrainLine;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@NullMarked
public interface DetailedAlertsApi {
    List<Alert> list(AlertsQuery query);

    default List<Alert> list() {
        AlertsQuery query = AlertsQuery.builder()
                                       .build();

        return this.list(query);
    }

    List<Alert> findByBusRouteIds(BusRouteAlertsQuery query);

    default List<Alert> findByBusRouteIds(Collection<String> routeIds) {
        Objects.requireNonNull(routeIds);

        List<String> routeIdsList = List.copyOf(routeIds);

        BusRouteAlertsQuery query = BusRouteAlertsQuery.builder(routeIdsList)
                                                       .build();

        return this.findByBusRouteIds(query);
    }

    default List<Alert> findByBusRouteId(String routeId) {
        Objects.requireNonNull(routeId);

        List<String> routeIds = List.of(routeId);

        return this.findByBusRouteIds(routeIds);
    }

    List<Alert> findByLines(LineAlertsQuery query);

    default List<Alert> findByLines(Collection<TrainLine> lines) {
        Objects.requireNonNull(lines);

        List<TrainLine> linesList = List.copyOf(lines);

        LineAlertsQuery query = LineAlertsQuery.builder(linesList)
                                               .build();

        return this.findByLines(query);
    }

    default List<Alert> findByLine(TrainLine line) {
        Objects.requireNonNull(line);

        List<TrainLine> lines = List.of(line);

        return this.findByLines(lines);
    }

    List<Alert> findByStationIds(StationAlertsQuery query);

    default List<Alert> findByStationIds(Collection<String> stationIds) {
        Objects.requireNonNull(stationIds);

        List<String> stationIdsList = List.copyOf(stationIds);

        StationAlertsQuery query = StationAlertsQuery.builder(stationIdsList)
                                                     .build();

        return this.findByStationIds(query);
    }

    default List<Alert> findByStationId(String stationId) {
        Objects.requireNonNull(stationId);

        List<String> stationIds = List.of(stationId);

        return this.findByStationIds(stationIds);
    }
}
